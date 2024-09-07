package fun.sast.evento.lark.domain.lark.service.impl;

import com.lark.oapi.service.calendar.v4.model.*;
import fun.sast.evento.lark.domain.lark.service.LarkEventService;
import fun.sast.evento.lark.domain.lark.value.LarkEventCreate;
import fun.sast.evento.lark.domain.lark.value.LarkEventModify;
import fun.sast.evento.lark.infrastructure.error.BusinessException;
import fun.sast.evento.lark.infrastructure.error.ErrorEnum;
import fun.sast.evento.lark.infrastructure.lark.OApi;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

            // TODO: Check room and group availability
            CalendarEventAttendee[] attendees = {
                    CalendarEventAttendee.newBuilder()
                            .type("resource")
                            .roomId(create.roomId())
                            .build(),
                    CalendarEventAttendee.newBuilder()
                            .type("chat")
                            .roomId(create.groupId())
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
                throw new BusinessException(ErrorEnum.LARK_ERROR, createCalendarEventResp.getMsg());
            }

            return id;
        } catch (Exception e) {
            throw new BusinessException(ErrorEnum.LARK_ERROR, e.getMessage());
        }
    }

    @Override
    public void modify(String id, LarkEventModify modify) {
    }

    @Override
    public void delete(String id) {
    }
}
