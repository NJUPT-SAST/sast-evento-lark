package fun.sast.evento.lark.domain.lark.service.impl;

import com.lark.oapi.service.calendar.v4.model.*;
import fun.sast.evento.lark.domain.lark.service.LarkEventService;
import fun.sast.evento.lark.domain.lark.value.LarkEventCreate;
import fun.sast.evento.lark.domain.lark.value.LarkEventUpdate;
import fun.sast.evento.lark.infrastructure.error.BusinessException;
import fun.sast.evento.lark.infrastructure.error.ErrorEnum;
import fun.sast.evento.lark.infrastructure.lark.OApi;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LarkEventServiceImpl implements LarkEventService {

    @Resource
    private OApi oApi;
    @Value("${app.lark.calendar-id}")
    private String calendarId;

    @Override
    public String create(LarkEventCreate create) {
        CalendarEvent calendarEvent = new CalendarEvent.Builder()
                .summary(create.summary())
                .description(create.description())
                .startTime(create.start())
                .endTime(create.end())
                .build();
        try {
            CreateCalendarEventResp createCalendarEventResp = oApi.getClient().calendar().calendarEvent().create(CreateCalendarEventReq.newBuilder()
                    .calendarId(calendarId)
                    .calendarEvent(calendarEvent)
                    .build());
            if (!createCalendarEventResp.success()) {
                throw new BusinessException(ErrorEnum.LARK_ERROR, createCalendarEventResp.getMsg());
            }
            String id = createCalendarEventResp.getData().getEvent().getEventId();

            /*
            "user"：User
            "chat"：Group
            "resource"：Room
            "third_party"：Email
             */
            CalendarEventAttendee[] attendees = {
                    CalendarEventAttendee.newBuilder()
                            .type("resource")
                            .roomId(create.roomId())
                            .build(),
                    CalendarEventAttendee.newBuilder()
                            .type("chat")
                            .chatId(create.groupId())
                            .build()
            };
            CreateCalendarEventAttendeeResp createCalendarEventAttendeeResp = oApi.getClient().calendar().calendarEventAttendee().create(CreateCalendarEventAttendeeReq.newBuilder()
                    .calendarId(calendarId)
                    .eventId(id)
                    .createCalendarEventAttendeeReqBody(CreateCalendarEventAttendeeReqBody.newBuilder()
                            .attendees(attendees)
                            .build())
                    .build());
            if (!createCalendarEventAttendeeResp.success()) {
                throw new BusinessException(ErrorEnum.LARK_ERROR, createCalendarEventAttendeeResp.getMsg());
            }

            return id;
        } catch (Exception e) {
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
            return resp.getData().getEvent();
        } catch (Exception e) {
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
                throw new BusinessException(ErrorEnum.LARK_ERROR, patchCalendarEventResp.getMsg());
            }

            ListCalendarEventAttendeeResp listCalendarEventAttendeeResp = oApi.getClient().calendar().calendarEventAttendee().list(ListCalendarEventAttendeeReq.newBuilder()
                    .calendarId(calendarId)
                    .eventId(id)
                    .build());
            if (!listCalendarEventAttendeeResp.success()) {
                throw new BusinessException(ErrorEnum.LARK_ERROR, listCalendarEventAttendeeResp.getMsg());
            }
            // Shouldn't have more pages
            List<String> deleteIds = new ArrayList<>();

            for (CalendarEventAttendee attendee : listCalendarEventAttendeeResp.getData().getItems()) {
                if (update.roomId() != null && attendee.getType().equals("resource")) {
                    deleteIds.add(attendee.getAttendeeId());
                }

                if (update.groupId() != null && attendee.getType().equals("chat")) {
                    deleteIds.add(attendee.getAttendeeId());
                }
            }
            BatchDeleteCalendarEventAttendeeResp batchDeleteCalendarEventAttendeeResp = oApi.getClient().calendar().calendarEventAttendee().batchDelete(BatchDeleteCalendarEventAttendeeReq.newBuilder()
                    .calendarId(calendarId)
                    .eventId(id)
                    .batchDeleteCalendarEventAttendeeReqBody(BatchDeleteCalendarEventAttendeeReqBody.newBuilder()
                            .attendeeIds(deleteIds.toArray(new String[0]))
                            .build())
                    .build());
            if (!batchDeleteCalendarEventAttendeeResp.success()) {
                throw new BusinessException(ErrorEnum.LARK_ERROR, listCalendarEventAttendeeResp.getMsg());
            }

            List<CalendarEventAttendee> attendees = new ArrayList<>();
            if (update.roomId() != null) {
                attendees.add(CalendarEventAttendee.newBuilder()
                        .type("resource")
                        .roomId(update.roomId())
                        .build());
            }
            if (update.groupId() != null) {
                attendees.add(CalendarEventAttendee.newBuilder()
                        .type("chat")
                        .chatId(update.groupId())
                        .build());
            }
            CreateCalendarEventAttendeeResp createCalendarEventAttendeeResp = oApi.getClient().calendar().calendarEventAttendee().create(CreateCalendarEventAttendeeReq.newBuilder()
                    .calendarId(calendarId)
                    .eventId(id)
                    .createCalendarEventAttendeeReqBody(CreateCalendarEventAttendeeReqBody.newBuilder()
                            .attendees(attendees.toArray(new CalendarEventAttendee[0]))
                            .build())
                    .build());
            if (!createCalendarEventAttendeeResp.success()) {
                throw new BusinessException(ErrorEnum.LARK_ERROR, createCalendarEventAttendeeResp.getMsg());
            }
        } catch (Exception e) {
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
                throw new BusinessException(ErrorEnum.LARK_ERROR, resp.getMsg());
            }
        } catch (Exception e) {
            throw new BusinessException(ErrorEnum.LARK_ERROR, e.getMessage());
        }
    }
}
