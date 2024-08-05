package fun.sast.evento.lark.domain.event.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import fun.sast.evento.lark.domain.common.value.Pagination;
import fun.sast.evento.lark.domain.event.entity.Event;
import fun.sast.evento.lark.domain.event.service.EventService;
import fun.sast.evento.lark.domain.event.value.EventCreate;
import fun.sast.evento.lark.domain.event.value.EventUpdate;
import fun.sast.evento.lark.domain.event.value.EventQuery;
import fun.sast.evento.lark.domain.lark.service.LarkEventService;
import fun.sast.evento.lark.infrastructure.error.BusinessException;
import fun.sast.evento.lark.infrastructure.repository.EventMapper;
import jakarta.annotation.Resource;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class EventServiceImpl implements EventService {

    @Resource
    private LarkEventService larkEventService;
    @Resource
    private EventMapper eventMapper;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private void validateAndSetTimes(Event event, String startStr, String endStr) throws BusinessException {
        try {
            LocalDateTime start = LocalDateTime.parse(startStr, formatter);
            LocalDateTime end = LocalDateTime.parse(endStr, formatter);
            if (start.isAfter(end)) {
                throw new BusinessException("start time should be before end time");
            }
            event.setStart(start);
            event.setEnd(end);
        } catch (DateTimeParseException e) {
            throw new BusinessException("wrong time format: " + e.getMessage());
        }
    }

    @Override
    public Event create(EventCreate create) {
        String larkEventUid = null; // TODO: Implement this
        Event event = new Event();
        validateAndSetTimes(event, create.start(), create.end());
        event.setSummary(create.summary());
        event.setDescription(create.description());
        event.setLocation(create.location());
        event.setTag(create.tag());
        event.setLarkEventUid(larkEventUid);
        eventMapper.insert(event);
        return event;
    }

    @Override
    public Event update(Integer id, EventUpdate update) {
        Event event = eventMapper.selectById(id);
        validateAndSetTimes(event, update.start(), update.end());
        event.setSummary(update.summary());
        event.setDescription(update.description());
        event.setLocation(update.location());
        event.setTag(update.tag());
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
        if (query.start() != null) {
            queryWrapper.ge("start", query.start());
        }
        if (query.end() != null) {
            queryWrapper.le("end", query.end());
        }
        if (query.location() != null) {
            queryWrapper.eq("location", query.location());
        }
        if (query.tag() != null) {
            queryWrapper.eq("tag", query.tag());
        }
        if (query.larkMeetingRoomId() != null) {
            queryWrapper.eq("lark_meeting_room_id", query.larkMeetingRoomId());
        }
        if (query.larkDepartmentId() != null) {
            queryWrapper.eq("lark_department_id", query.larkDepartmentId());
        }
        eventMapper.selectPage(page, queryWrapper);
        return new Pagination<>(page.getRecords(), page.getCurrent(), page.getTotal());
    }
}
