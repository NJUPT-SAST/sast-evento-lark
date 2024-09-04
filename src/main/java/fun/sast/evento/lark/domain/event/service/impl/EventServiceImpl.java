package fun.sast.evento.lark.domain.event.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lark.oapi.service.calendar.v4.model.TimeInfo;
import fun.sast.evento.lark.api.v2.value.V2;
import fun.sast.evento.lark.domain.common.value.EventState;
import fun.sast.evento.lark.domain.common.value.Pagination;
import fun.sast.evento.lark.domain.event.entity.Event;
import fun.sast.evento.lark.domain.event.service.EventService;
import fun.sast.evento.lark.domain.event.service.ParticipationService;
import fun.sast.evento.lark.domain.event.value.EventCreate;
import fun.sast.evento.lark.domain.event.value.EventQuery;
import fun.sast.evento.lark.domain.event.value.EventUpdate;
import fun.sast.evento.lark.domain.lark.service.LarkEventService;
import fun.sast.evento.lark.domain.lark.value.LarkEventCreate;
import fun.sast.evento.lark.infrastructure.error.BusinessException;
import fun.sast.evento.lark.infrastructure.error.ErrorEnum;
import fun.sast.evento.lark.infrastructure.repository.EventMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class EventServiceImpl implements EventService {

    @Resource
    private LarkEventService larkEventService;
    @Resource
    private ParticipationService participationService;
    @Resource
    private EventMapper eventMapper;

    private final ZoneId zone = ZoneId.systemDefault();

    @Override
    public Event create(EventCreate create) {
        String larkEventUid = larkEventService.create(new LarkEventCreate(
                create.summary(),
                create.description(),
                TimeInfo.newBuilder()
                        .timestamp(String.valueOf(create.start().atZone(zone).toInstant().toEpochMilli()))
                        .timezone(zone.toString())
                        .build(),
                TimeInfo.newBuilder()
                        .timestamp(String.valueOf(create.end().atZone(zone).toInstant().toEpochMilli()))
                        .timezone(zone.toString())
                        .build(),
                create.larkMeetingRoomId(),
                create.larkDepartmentId()
        ));
        String larkMeetingRoomName = null;  // TODO: Implement this
        String larkDepartmentName = null;  // TODO: Implement this
        LocalDateTime start = create.start();
        LocalDateTime end = create.end();
        if (start.isAfter(end)) {
            throw new BusinessException(ErrorEnum.PARAM_ERROR, "start time should be before end time");
        }
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
        return event;
    }

    @Override
    public Event update(Long eventId, EventUpdate update) {
        String larkMeetingRoomName = null;  // TODO: Implement this
        String larkDepartmentName = null;  // TODO: Implement this
        LocalDateTime start = update.start();
        LocalDateTime end = update.end();
        if (start.isAfter(end)) {
            throw new BusinessException(ErrorEnum.PARAM_ERROR, "start time should be before end time");
        }
        Event event = eventMapper.selectById(eventId);
        event.setSummary(update.summary());
        event.setDescription(update.description());

        event.setStart(start);
        event.setEnd(end);
        event.setLocation(update.location());
        event.setTag(update.tag());
        event.setLarkMeetingRoomName(larkMeetingRoomName);
        event.setLarkDepartmentName(larkDepartmentName);
        event.setCancelled(update.cancelled());
        eventMapper.updateById(event);
        return event;
    }

    @Override
    public Boolean delete(Long eventId) {
        return eventMapper.deleteById(eventId) > 0;
    }

    @Override
    public Event cancel(Long eventId) {
        Event event = eventMapper.selectById(eventId);
        event.setCancelled(true);
        eventMapper.updateById(event);
        return event;
    }

    @Override
    public Event get(Long eventId) {
        return eventMapper.selectById(eventId);
    }

    @Override
    public Pagination<Event> list(Integer current, Integer size) {
        Page<Event> page = new Page<>(current, size);
        eventMapper.selectPage(page, null);
        return new Pagination<>(page.getRecords(), page.getCurrent(), page.getTotal());
    }

    @Override
    public Pagination<Event> query(EventQuery query, Integer current, Integer size) {
        Page<Event> page = new Page<>(current, size);
        QueryWrapper<Event> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(query.id() != null, "id", query.id());
        queryWrapper.like(query.summary() != null, "summary", query.summary());
        queryWrapper.like(query.description() != null, "description", query.description());
        LocalDateTime now = LocalDateTime.now();
        if (Boolean.TRUE.equals(query.active())) {
            queryWrapper.le("start", now);
            queryWrapper.ge("end", now);
        } else {
            queryWrapper.ge(query.start() != null, "start", query.start());
            queryWrapper.le(query.end() != null, "end", query.end());
        }
        queryWrapper.eq(query.location() != null, "location", query.location());
        queryWrapper.eq(query.tag() != null, "tag", query.tag());
        queryWrapper.eq(query.larkMeetingRoomName() != null, "lark_meeting_room_name", query.larkMeetingRoomName());
        queryWrapper.eq(query.larkDepartmentName() != null, "lark_department_name", query.larkDepartmentName());
        eventMapper.selectPage(page, queryWrapper);
        return new Pagination<>(page.getRecords(), page.getCurrent(), page.getTotal());
    }

    @Override
    public EventState calculateState(Event event) {
        if (event.isCancelled()) {
            return EventState.CANCELLED;
        }
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(event.getStart())) {
            return EventState.SIGNING_UP;
        } else if (now.isAfter(event.getEnd())) {
            return EventState.COMPLETED;
        } else {
            return EventState.ACTIVE;
        }
    }

    @Override
    public V2.Event mapToV2Event(Event event) {
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
                participationService.isSubscribed(event.getId()),
                participationService.isCheckedIn(event.getId())
        );
    }
}
