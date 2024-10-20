package fun.sast.evento.lark.domain.event.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lark.oapi.service.calendar.v4.model.CalendarEventAttendee;
import com.lark.oapi.service.calendar.v4.model.TimeInfo;
import fun.sast.evento.lark.api.value.V2;
import fun.sast.evento.lark.domain.common.value.Constants;
import fun.sast.evento.lark.domain.common.value.EventState;
import fun.sast.evento.lark.domain.common.value.Pagination;
import fun.sast.evento.lark.domain.event.entity.Event;
import fun.sast.evento.lark.domain.event.service.EventService;
import fun.sast.evento.lark.domain.event.service.SubscriptionService;
import fun.sast.evento.lark.domain.event.value.EventCreate;
import fun.sast.evento.lark.domain.event.value.EventQuery;
import fun.sast.evento.lark.domain.event.value.EventUpdate;
import fun.sast.evento.lark.domain.lark.service.LarkDepartmentService;
import fun.sast.evento.lark.domain.lark.service.LarkEventService;
import fun.sast.evento.lark.domain.lark.service.LarkRoomService;
import fun.sast.evento.lark.domain.lark.service.LarkUserService;
import fun.sast.evento.lark.domain.lark.value.LarkEventCreate;
import fun.sast.evento.lark.domain.lark.value.LarkEventUpdate;
import fun.sast.evento.lark.domain.lark.value.LarkUser;
import fun.sast.evento.lark.domain.subscription.service.MessageService;
import fun.sast.evento.lark.infrastructure.auth.JWTInterceptor;
import fun.sast.evento.lark.infrastructure.error.BusinessException;
import fun.sast.evento.lark.infrastructure.error.ErrorEnum;
import fun.sast.evento.lark.infrastructure.repository.EventMapper;
import fun.sast.evento.lark.infrastructure.utils.TimeUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    @Resource
    private LarkDepartmentService larkDepartmentService;
    @Resource
    private LarkEventService larkEventService;
    @Resource
    private LarkRoomService larkRoomService;
    @Resource
    private LarkUserService larkUserService;
    @Resource
    private SubscriptionService subscriptionService;
    @Resource
    private MessageService messageService;
    @Resource
    private EventMapper eventMapper;
    @Value("${app.lark.check-room-availability}")
    private Boolean checkRoomAvailability;

    @Override
    public Event create(EventCreate create) {
        if (checkRoomAvailability && create.larkMeetingRoomId() != null && !larkRoomService.isAvailable(create.larkMeetingRoomId(), create.start(), create.end())) {
            throw new BusinessException(ErrorEnum.PARAM_ERROR, "meeting room is not available");
        }
        if (create.larkDepartmentId() == null) {
            throw new BusinessException(ErrorEnum.PARAM_ERROR, "department id cannot be null");
        }
        String larkMeetingRoomName = create.larkMeetingRoomId() == null ? null : larkRoomService.get(create.larkMeetingRoomId()).name();
        String larkDepartmentName = larkDepartmentService.get(create.larkDepartmentId()).name();
        LocalDateTime start = create.start();
        LocalDateTime end = create.end();
        if (start.isAfter(end)) {
            throw new BusinessException(ErrorEnum.PARAM_ERROR, "start time should be before end time");
        }
        String larkEventUid = larkEventService.create(new LarkEventCreate(
                create.summary(),
                create.description(),
                TimeInfo.newBuilder()
                        .timestamp(TimeUtils.toEpochSecond(create.start()))
                        .timezone(TimeUtils.zone())
                        .build(),
                TimeInfo.newBuilder()
                        .timestamp(TimeUtils.toEpochSecond(create.end()))
                        .timezone(TimeUtils.zone())
                        .build(),
                create.larkMeetingRoomId(),
                create.larkDepartmentId()
        ));
        Event event = new Event();
        event.setSummary(create.summary());
        event.setDescription(create.description());

        event.setStart(start);
        event.setEnd(end);
        event.setLocation(create.location());
        event.setTag(create.tag());
        event.setLarkEventUid(larkEventUid);
        event.setLarkMeetingRoomName(larkMeetingRoomName);
        event.setLarkDepartmentName(larkDepartmentName);
        eventMapper.insert(event);
        subscriptionService.getSubscribedUsers(create.larkDepartmentId()).forEach(user -> subscriptionService.subscribeEvent(event.getId(), user, true));
        scheduleStateUpdate(event);
        return event;
    }

    @Override
    public Event update(Long eventId, EventUpdate update) {
        Event event = eventMapper.selectById(eventId);
        if (event == null) {
            throw new BusinessException(ErrorEnum.PARAM_ERROR, "event not found");
        }
        // if room == null -> remove room
        // if room != null && room != old room -> update room
        LocalDateTime start = update.start() == null ? event.getStart() : update.start();
        LocalDateTime end = update.end() == null ? event.getEnd() : update.end();
        if (start.isAfter(end)) {
            throw new BusinessException(ErrorEnum.PARAM_ERROR, "start time should be before end time");
        }
        String larkMeetingRoomName = update.larkMeetingRoomId() == null ? null : larkRoomService.get(update.larkMeetingRoomId()).name();
        if (checkRoomAvailability && update.larkMeetingRoomId() != null &&
                !larkMeetingRoomName.equals(event.getLarkMeetingRoomName()) &&
                !larkRoomService.isAvailable(update.larkMeetingRoomId(), start, end)) {
            throw new BusinessException(ErrorEnum.PARAM_ERROR, "meeting room is not available");
        }
        larkEventService.update(event.getLarkEventUid(), new LarkEventUpdate(
                update.summary(),
                update.description(),
                update.start() == null ? null : TimeInfo.newBuilder()
                        .timestamp(TimeUtils.toEpochSecond(start))
                        .timezone(TimeUtils.zone())
                        .build(),
                update.end() == null ? null : TimeInfo.newBuilder()
                        .timestamp(TimeUtils.toEpochSecond(end))
                        .timezone(TimeUtils.zone())
                        .build(),
                update.larkMeetingRoomId()
        ));
        if (update.summary() != null) event.setSummary(update.summary());
        if (update.description() != null) event.setDescription(update.description());

        if (update.start() != null) event.setStart(start);
        if (update.end() != null) event.setEnd(end);
        if (update.location() != null) event.setLocation(update.location());
        if (update.tag() != null) event.setTag(update.tag());
        if (larkMeetingRoomName != null) event.setLarkMeetingRoomName(larkMeetingRoomName);
        if (update.cancelled() != null) event.setCancelled(update.cancelled());
        eventMapper.updateById(event);
        scheduleStateUpdate(event);
        return event;
    }

    @Override
    public Boolean delete(Long eventId) {
        Event event = eventMapper.selectById(eventId);
        if (event == null) {
            throw new BusinessException(ErrorEnum.PARAM_ERROR, "event not found");
        }
        larkEventService.delete(event.getLarkEventUid());
        subscriptionService.delete(eventId);
        return eventMapper.deleteById(eventId) > 0;
    }

    @Override
    public Event cancel(Long eventId) {
        Event event = eventMapper.selectById(eventId);
        larkEventService.delete(event.getLarkEventUid());
        event.setCancelled(true);
        eventMapper.updateById(event);
        messageService.schedule(eventId, EventState.CANCELLED, LocalDateTime.now());
        return event;
    }

    @Override
    public Event get(Long eventId) {
        return eventMapper.selectById(eventId);
    }

    @Override
    public Pagination<Event> list(Integer current, Integer size) {
        return new Pagination<>(eventMapper.selectList(new Page<>(current, size), null), current, size);
    }

    @Override
    public Pagination<Event> query(EventQuery query, Integer current, Integer size) {
        Page<Event> page = new Page<>(current, size);
        QueryWrapper<Event> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(query.id() != null, "id", query.id());
        queryWrapper.like(query.summary() != null && !query.summary().isEmpty(), "summary", query.summary());
        queryWrapper.like(query.description() != null && !query.description().isEmpty(), "description", query.description());
        LocalDateTime now = LocalDateTime.now();
        if (Boolean.TRUE.equals(query.active())) {
            queryWrapper.le("start", now);
            queryWrapper.ge("end", now);
        } else {
            queryWrapper.ge(query.start() != null, "start", query.start());
            queryWrapper.le(query.end() != null, "end", query.end());
        }
        queryWrapper.eq(query.location() != null && !query.location().isEmpty(), "location", query.location());
        queryWrapper.eq(query.tag() != null && !query.tag().isEmpty(), "tag", query.tag());
        queryWrapper.eq(query.larkMeetingRoomName() != null && !query.larkMeetingRoomName().isEmpty(), "lark_meeting_room_name", query.larkMeetingRoomName());
        queryWrapper.eq(query.larkDepartmentName() != null && !query.larkDepartmentName().isEmpty(), "lark_department_name", query.larkDepartmentName());
        queryWrapper.exists(Boolean.TRUE.equals(query.participated()),
                "SELECT 1 FROM subscription WHERE subscription.event_id = event.id AND subscription.link_id = " +
                        JWTInterceptor.userHolder.get().getUserId() +
                        " AND subscription.checked_in = true");
        queryWrapper.exists(Boolean.TRUE.equals(query.subscribed()),
                "SELECT 1 FROM subscription WHERE subscription.event_id = event.id AND subscription.link_id = " +
                        JWTInterceptor.userHolder.get().getUserId() +
                        " AND subscription.subscribed = true");
        eventMapper.selectPage(page, queryWrapper);
        return new Pagination<>(page.getRecords(), page.getCurrent(), page.getTotal());
    }

    @Override
    public EventState calculateState(Event event) {
        if (event.isCancelled()) {
            return EventState.CANCELLED;
        }
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(event.getStart().minusMinutes(30))) {
            return EventState.SIGNING_UP;
        } else if (now.isBefore(event.getStart())) {
            return EventState.UPCOMING;
        } else if (now.isAfter(event.getEnd())) {
            return EventState.COMPLETED;
        } else {
            return EventState.ACTIVE;
        }
    }

    @Override
    public void invite(Long eventId, List<V2.LarkUser> users) {
        Event event = eventMapper.selectById(eventId);
        if (event == null) {
            throw new BusinessException(ErrorEnum.PARAM_ERROR, "event not found");
        }
        larkEventService.invite(event.getLarkEventUid(), users);
    }

    @Override
    public List<LarkUser> getAttendees(Long eventId) {
        Event event = eventMapper.selectById(eventId);
        if (event == null) {
            throw new BusinessException(ErrorEnum.PARAM_ERROR, "event not found");
        }
        return larkUserService.list(larkEventService.getAttendees(event.getLarkEventUid(), Constants.LARK_ATTENDEE_TYPE_USER)
                .stream()
                .map(CalendarEventAttendee::getUserId)
                .toList());
    }

    private void scheduleStateUpdate(Event event) {
        messageService.schedule(event.getId(), EventState.UPCOMING, event.getStart().minusMinutes(30));
        messageService.schedule(event.getId(), EventState.ACTIVE, event.getStart());
        messageService.schedule(event.getId(), EventState.COMPLETED, event.getEnd());
    }

    @Override
    public V2.Event mapToV2(Event event) {
        return new V2.Event(
                event.getId(),
                event.getSummary(),
                event.getDescription(),
                event.getStart(),
                event.getEnd(),
                calculateState(event),
                event.getLocation(),
                event.getTag(),
                event.getLarkMeetingRoomName(),
                event.getLarkDepartmentName(),
                subscriptionService.isSubscribed(event.getId(), JWTInterceptor.userHolder.get().getUserId()),
                subscriptionService.isCheckedIn(event.getId(), JWTInterceptor.userHolder.get().getUserId())
        );
    }
}
