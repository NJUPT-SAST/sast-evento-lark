package fun.sast.evento.lark.domain.lark.service.impl;

import com.lark.oapi.service.calendar.v4.model.*;
import fun.sast.evento.lark.domain.lark.service.LarkEventService;
import fun.sast.evento.lark.domain.lark.value.LarkEventCreate;
import fun.sast.evento.lark.domain.lark.value.LarkEventUpdate;
import fun.sast.evento.lark.infrastructure.error.BusinessException;
import fun.sast.evento.lark.infrastructure.error.ErrorEnum;
import fun.sast.evento.lark.infrastructure.lark.OApi;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class LarkEventServiceImpl implements LarkEventService {

    @Resource
    private OApi oApi;
    @Value("${app.lark.calendar-id}")
    private String calendarId;
    @Resource
    private LarkDepartmentServiceImpl larkDepartmentServiceImpl;

    @Override
    public String create(LarkEventCreate create) {
        CalendarEvent calendarEvent = new CalendarEvent.Builder()
                .summary(create.summary())
                .description(create.description())
                .startTime(create.start())
                .endTime(create.end())
                .attendeeAbility("can_invite_others")
                .build();
        try {
            CreateCalendarEventResp createCalendarEventResp = oApi.getClient().calendar().calendarEvent().create(CreateCalendarEventReq.newBuilder()
                    .calendarId(calendarId)
                    .calendarEvent(calendarEvent)
                    .build());
            if (!createCalendarEventResp.success()) {
                log.error("failed to create event: {}", createCalendarEventResp.getMsg());
                throw new BusinessException(ErrorEnum.LARK_ERROR, createCalendarEventResp.getMsg());
            }
            String id = createCalendarEventResp.getData().getEvent().getEventId();

            /*
            "user"：User
            "chat"：Group
            "resource"：Room
            "third_party"：Email
             */
            List<CalendarEventAttendee> attendeeList = new ArrayList<>();
            attendeeList.add(CalendarEventAttendee.newBuilder()
                    .type("resource")
                    .roomId(create.roomId())
                    .build());
            CalendarEventAttendee[] attendees = attendeeList.toArray(new CalendarEventAttendee[0]);
            CreateCalendarEventAttendeeResp createCalendarEventAttendeeResp = oApi.getClient().calendar().calendarEventAttendee().create(CreateCalendarEventAttendeeReq.newBuilder()
                    .calendarId(calendarId)
                    .eventId(id)
                    .createCalendarEventAttendeeReqBody(CreateCalendarEventAttendeeReqBody.newBuilder()
                            .attendees(attendees)
                            .build())
                    .build());
            if (!createCalendarEventAttendeeResp.success()) {
                delete(id);
                log.error("failed to create event attendee: {}", createCalendarEventAttendeeResp.getMsg());
                throw new BusinessException(ErrorEnum.LARK_ERROR, createCalendarEventAttendeeResp.getMsg());
            }

            return id;
        } catch (Exception e) {
            log.error("create event error: {}", e.getMessage());
            throw new BusinessException(ErrorEnum.LARK_ERROR, e.getMessage());
        }
    }

    @Override
    public CalendarEvent get(String id) {
        try {
            GetCalendarEventResp resp = oApi.getClient().calendar().calendarEvent().get(GetCalendarEventReq.newBuilder()
                    .calendarId(calendarId)
                    .eventId(id)
                    .build());
            if (!resp.success()) {
                log.error("failed to get event: {}", resp.getMsg());
                throw new BusinessException(ErrorEnum.LARK_ERROR, resp.getMsg());
            }
            return resp.getData().getEvent();
        } catch (Exception e) {
            log.error("get event error: {}", e.getMessage());
            throw new BusinessException(ErrorEnum.LARK_ERROR, e.getMessage());
        }
    }

    @Override
    public void update(String id, LarkEventUpdate update) {
        CalendarEvent event = get(id);
        if (event == null) {
            throw new BusinessException(ErrorEnum.LARK_ERROR, "event not found");
        }
        if (update.summary() != null) event.setSummary(update.summary());
        if (update.description() != null) event.setDescription(update.description());
        if (update.start() != null) event.setStartTime(update.start());
        if (update.end() != null) event.setEndTime(update.end());
        try {
            PatchCalendarEventResp patchCalendarEventResp = oApi.getClient().calendar().calendarEvent().patch(PatchCalendarEventReq.newBuilder()
                    .calendarId(calendarId)
                    .eventId(id)
                    .calendarEvent(event)
                    .build());
            if (!patchCalendarEventResp.success()) {
                log.error("failed to update event: {}", patchCalendarEventResp.getMsg());
                throw new BusinessException(ErrorEnum.LARK_ERROR, patchCalendarEventResp.getMsg());
            }

            if (update.roomId() != null) {
                // Get the old meeting room
                ListCalendarEventAttendeeResp listCalendarEventAttendeeResp = oApi.getClient().calendar().calendarEventAttendee().list(ListCalendarEventAttendeeReq.newBuilder()
                        .calendarId(calendarId)
                        .pageSize(100)
                        .eventId(id)
                        .build());
                if (!listCalendarEventAttendeeResp.success()) {
                    log.error("failed to list event attendee: {}", listCalendarEventAttendeeResp.getMsg());
                    throw new BusinessException(ErrorEnum.LARK_ERROR, listCalendarEventAttendeeResp.getMsg());
                }
                // Shouldn't have more pages
                List<CalendarEventAttendeeId> deleteIds = new ArrayList<>();

                for (CalendarEventAttendee attendee : listCalendarEventAttendeeResp.getData().getItems()) {
                    if (attendee.getType().equals("resource")) {
                        deleteIds.add(CalendarEventAttendeeId.newBuilder().type("resource").roomId(attendee.getRoomId()).build());
                    }
                }
                if (!deleteIds.isEmpty()) {
                    // delete the old meeting room
                    BatchDeleteCalendarEventAttendeeResp batchDeleteCalendarEventAttendeeResp = oApi.getClient().calendar().calendarEventAttendee().batchDelete(BatchDeleteCalendarEventAttendeeReq.newBuilder()
                            .calendarId(calendarId)
                            .eventId(id)
                            .batchDeleteCalendarEventAttendeeReqBody(BatchDeleteCalendarEventAttendeeReqBody.newBuilder()
                                    .deleteIds(deleteIds.toArray(new CalendarEventAttendeeId[0]))
                                    .build())
                            .build());
                    if (!batchDeleteCalendarEventAttendeeResp.success()) {
                        log.error("failed to delete old event attendee: {}", batchDeleteCalendarEventAttendeeResp.getMsg());
                        throw new BusinessException(ErrorEnum.LARK_ERROR, batchDeleteCalendarEventAttendeeResp.getMsg());
                    }
                }

                // add the new meeting room
                CalendarEventAttendee[] attendees = {
                        CalendarEventAttendee.newBuilder()
                                .type("resource")
                                .roomId(update.roomId())
                                .build()
                };
                CreateCalendarEventAttendeeResp createCalendarEventAttendeeResp = oApi.getClient().calendar().calendarEventAttendee().create(CreateCalendarEventAttendeeReq.newBuilder()
                        .calendarId(calendarId)
                        .eventId(id)
                        .createCalendarEventAttendeeReqBody(CreateCalendarEventAttendeeReqBody.newBuilder()
                                .attendees(attendees)
                                .needNotification(true)
                                .build())
                        .build());
                if (!createCalendarEventAttendeeResp.success()) {
                    log.error("failed to create new event attendee: {}", createCalendarEventAttendeeResp.getMsg());
                    throw new BusinessException(ErrorEnum.LARK_ERROR, createCalendarEventAttendeeResp.getMsg());
                }
            }
        } catch (Exception e) {
            log.error("update event error: {}", e.getMessage());
            throw new BusinessException(ErrorEnum.LARK_ERROR, e.getMessage());
        }
    }

    @Override
    public void delete(String id) {
        try {
            DeleteCalendarEventResp resp = oApi.getClient().calendar().calendarEvent().delete(DeleteCalendarEventReq.newBuilder()
                    .calendarId(calendarId)
                    .eventId(id)
                    .build());
            if (!resp.success()) {
                log.error("failed to delete event: {}", resp.getMsg());
                throw new BusinessException(ErrorEnum.LARK_ERROR, resp.getMsg());
            }
        } catch (Exception e) {
            log.error("delete event error: {}", e.getMessage());
            throw new BusinessException(ErrorEnum.LARK_ERROR, e.getMessage());
        }
    }
}
