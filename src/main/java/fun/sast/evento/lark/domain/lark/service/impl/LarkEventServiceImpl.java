package fun.sast.evento.lark.domain.lark.service.impl;

import com.lark.oapi.service.calendar.v4.model.*;
import fun.sast.evento.lark.api.value.V2;
import fun.sast.evento.lark.domain.common.value.Constants;
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

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

            if (create.roomId() != null) {
                CalendarEventAttendee[] attendees = new CalendarEventAttendee[]{
                        CalendarEventAttendee.newBuilder()
                                .type(Constants.LARK_ATTENDEE_TYPE_ROOM)
                                .roomId(create.roomId())
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
                    delete(id);
                    log.error("failed to create event attendee: {}", createCalendarEventAttendeeResp.getMsg());
                    throw new BusinessException(ErrorEnum.LARK_ERROR, createCalendarEventAttendeeResp.getMsg());
                }
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
                CalendarEventAttendeeId[] deleteIds = getAttendees(event.getEventId(), Constants.LARK_ATTENDEE_TYPE_ROOM)
                        .stream()
                        .map(attendee -> CalendarEventAttendeeId.newBuilder()
                                .type(Constants.LARK_ATTENDEE_TYPE_ROOM)
                                .roomId(attendee.getRoomId())
                                .build())
                        .toArray(CalendarEventAttendeeId[]::new);
                if (deleteIds.length > 0) {
                    // delete the old meeting room
                    BatchDeleteCalendarEventAttendeeResp batchDeleteCalendarEventAttendeeResp = oApi.getClient().calendar().calendarEventAttendee().batchDelete(BatchDeleteCalendarEventAttendeeReq.newBuilder()
                            .calendarId(calendarId)
                            .eventId(id)
                            .batchDeleteCalendarEventAttendeeReqBody(BatchDeleteCalendarEventAttendeeReqBody.newBuilder()
                                    .deleteIds(deleteIds)
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
                                .type(Constants.LARK_ATTENDEE_TYPE_ROOM)
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

    @Override
    public void invite(String id, List<V2.LarkUser> users) {
        try {
            Set<CalendarEventAttendee> newAttendees = users
                    .stream()
                    .map(user -> CalendarEventAttendee.newBuilder()
                            .type(Constants.LARK_ATTENDEE_TYPE_USER)
                            .userId(user.openId())
                            .build())
                    .collect(Collectors.toSet());
            Set<CalendarEventAttendee> oldAttendees = getAttendees(id, Constants.LARK_ATTENDEE_TYPE_USER);

            Set<CalendarEventAttendee> toAdd = new HashSet<>(newAttendees);
            toAdd.removeAll(oldAttendees);

            Set<CalendarEventAttendee> toDelete = new HashSet<>(oldAttendees);
            toDelete.removeAll(newAttendees);

            if (!toAdd.isEmpty()) {
                CalendarEventAttendee[] attendees = toAdd.toArray(new CalendarEventAttendee[0]);
                CreateCalendarEventAttendeeResp createCalendarEventAttendeeResp = oApi.getClient().calendar().calendarEventAttendee().create(CreateCalendarEventAttendeeReq.newBuilder()
                        .calendarId(calendarId)
                        .eventId(id)
                        .createCalendarEventAttendeeReqBody(CreateCalendarEventAttendeeReqBody.newBuilder()
                                .attendees(attendees)
                                .needNotification(true)
                                .build())
                        .build());
                if (!createCalendarEventAttendeeResp.success()) {
                    log.error("failed to invite user to event: {}", createCalendarEventAttendeeResp.getMsg());
                    throw new BusinessException(ErrorEnum.LARK_ERROR, createCalendarEventAttendeeResp.getMsg());
                }
            }

            if (!toDelete.isEmpty()) {
                CalendarEventAttendeeId[] deleteIds = toDelete
                        .stream()
                        .map(attendee -> CalendarEventAttendeeId.newBuilder()
                                .type(Constants.LARK_ATTENDEE_TYPE_USER)
                                .userId(attendee.getUserId())
                                .build())
                        .toArray(CalendarEventAttendeeId[]::new);
                BatchDeleteCalendarEventAttendeeResp batchDeleteCalendarEventAttendeeResp = oApi.getClient().calendar().calendarEventAttendee().batchDelete(BatchDeleteCalendarEventAttendeeReq.newBuilder()
                        .calendarId(calendarId)
                        .eventId(id)
                        .batchDeleteCalendarEventAttendeeReqBody(BatchDeleteCalendarEventAttendeeReqBody.newBuilder()
                                .deleteIds(deleteIds)
                                .build())
                        .build());
                if (!batchDeleteCalendarEventAttendeeResp.success()) {
                    log.error("failed to delete user from event: {}", batchDeleteCalendarEventAttendeeResp.getMsg());
                    throw new BusinessException(ErrorEnum.LARK_ERROR, batchDeleteCalendarEventAttendeeResp.getMsg());
                }
            }
        } catch (Exception e) {
            log.error("invite user to event error: {}", e.getMessage());
            throw new BusinessException(ErrorEnum.LARK_ERROR, e.getMessage());
        }
    }

    @Override
    public Set<CalendarEventAttendee> getAttendees(String id, String type) {
        try {
            Set<CalendarEventAttendee> result = new HashSet<>();
            String pageToken = null;
            boolean hasMore;
            do {
                ListCalendarEventAttendeeResp resp = oApi.getClient().calendar().calendarEventAttendee().list(ListCalendarEventAttendeeReq.newBuilder()
                        .calendarId(calendarId)
                        .eventId(id)
                        .pageSize(100)
                        .pageToken(pageToken)
                        .build());
                if (!resp.success()) {
                    log.error("failed to list event attendee: {}", resp.getMsg());
                    throw new BusinessException(ErrorEnum.LARK_ERROR, resp.getMsg());
                }
                pageToken = resp.getData().getPageToken();
                hasMore = resp.getData().getHasMore();
                if (resp.getData().getItems() != null) {
                    for (CalendarEventAttendee attendee : resp.getData().getItems()) {
                        if (attendee.getType().equals(type)) {
                            result.add(attendee);
                        }
                    }
                }
            } while (hasMore);
            return result;
        } catch (Exception e) {
            log.error("list event attendee error: {}", e.getMessage());
            throw new BusinessException(ErrorEnum.LARK_ERROR, e.getMessage());
        }
    }
}
