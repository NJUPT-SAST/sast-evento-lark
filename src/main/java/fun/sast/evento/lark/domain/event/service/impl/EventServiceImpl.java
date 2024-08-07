package fun.sast.evento.lark.domain.event.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
import fun.sast.evento.lark.infrastructure.auth.JWTInterceptor;
import fun.sast.evento.lark.infrastructure.error.BusinessException;
import fun.sast.evento.lark.infrastructure.repository.EventMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    @Resource
    private LarkEventService larkEventService;
    @Resource
    private ParticipationService participationService;
    @Resource
    private EventMapper eventMapper;

    @Override
    public Event create(EventCreate create) {
        String larkEventUid = null; // TODO: Implement this
        String larkMeetingRoomName = null;  // TODO: Implement this
        String larkDepartmentName = null;  // TODO: Implement this
        LocalDateTime start = create.start();
        LocalDateTime end = create.end();
        if (start.isAfter(end)) {
            throw new BusinessException("start time should be before end time");
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
    public Event update(Integer id, EventUpdate update) {
        String larkMeetingRoomName = null;  // TODO: Implement this
        String larkDepartmentName = null;  // TODO: Implement this
        LocalDateTime start = update.start();
        LocalDateTime end = update.end();
        if (start.isAfter(end)) {
            throw new BusinessException("start time should be before end time");
        }
        Event event = eventMapper.selectById(id);
        event.setSummary(update.summary());
        event.setDescription(update.description());

        event.setStart(start);
        event.setEnd(end);
        event.setLocation(update.location());
        event.setTag(update.tag());
        event.setLarkMeetingRoomName(larkMeetingRoomName);
        event.setLarkDepartmentName(larkDepartmentName);
        eventMapper.updateById(event);
        return event;
    }

    @Override
    public Boolean delete(Integer id) {
        return eventMapper.deleteById(id) > 0;
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
        if (query.id() != null) {
            queryWrapper.eq("id", query.id());
        }
        if (query.summary() != null) {
            queryWrapper.eq("summary", query.summary());
        }
        if (query.description() != null) {
            queryWrapper.like("description", query.description());
        }
        LocalDateTime now = LocalDateTime.now();
        if (Boolean.TRUE.equals(query.active())) {
            queryWrapper.le("start", now);
            queryWrapper.ge("end", now);
        } else {
            if (query.start() != null) {
                queryWrapper.ge("start", query.start());
            }
            if (query.end() != null) {
                queryWrapper.le("end", query.end());
            }
        }
        if (query.location() != null) {
            queryWrapper.eq("location", query.location());
        }
        if (query.tag() != null) {
            queryWrapper.eq("tag", query.tag());
        }
        if (query.larkMeetingRoomName() != null) {
            queryWrapper.eq("lark_meeting_room_name", query.larkMeetingRoomName());
        }
        if (query.larkDepartmentName() != null) {
            queryWrapper.eq("lark_department_name", query.larkDepartmentName());
        }
        eventMapper.selectPage(page, queryWrapper);
        return new Pagination<>(page.getRecords(), page.getCurrent(), page.getTotal());
    }

    @Override
    public List<Event> query(EventQuery query) {
        QueryWrapper<Event> queryWrapper = new QueryWrapper<>();
        if (query.id() != null) {
            queryWrapper.eq("id", query.id());
        }
        if (query.summary() != null) {
            queryWrapper.eq("summary", query.summary());
        }
        if (query.description() != null) {
            queryWrapper.like("description", query.description());
        }
        LocalDateTime now = LocalDateTime.now();
        if (Boolean.TRUE.equals(query.active())) {
            queryWrapper.le("start", now);
            queryWrapper.ge("end", now);
        } else {
            if (query.start() != null) {
                queryWrapper.ge("start", query.start());
            }
            if (query.end() != null) {
                queryWrapper.le("end", query.end());
            }
        }
        if (query.location() != null) {
            queryWrapper.eq("location", query.location());
        }
        if (query.tag() != null) {
            queryWrapper.eq("tag", query.tag());
        }
        if (query.larkMeetingRoomName() != null) {
            queryWrapper.eq("lark_meeting_room_name", query.larkMeetingRoomName());
        }
        if (query.larkDepartmentName() != null) {
            queryWrapper.eq("lark_department_name", query.larkDepartmentName());
        }
        return eventMapper.selectList(queryWrapper);
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
        String linkId = JWTInterceptor.userHolder.get().getUserId();
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
                participationService.isSubscribed(event.getId(), linkId),
                participationService.isCheckedIn(event.getId(), linkId)
        );
    }
}
