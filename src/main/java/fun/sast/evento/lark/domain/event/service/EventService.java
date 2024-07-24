package fun.sast.evento.lark.domain.event.service;

import fun.sast.evento.lark.domain.common.value.Pagination;
import fun.sast.evento.lark.domain.event.entity.Event;
import fun.sast.evento.lark.domain.event.value.EventCreate;
import fun.sast.evento.lark.domain.event.value.EventModify;
import fun.sast.evento.lark.domain.event.value.EventQuery;

public interface EventService {
    Event create(EventCreate create);

    Boolean modify(Integer id, EventModify modify);

    Boolean delete(Integer id);

    Pagination<Event> list(Integer current, Integer size);

    Pagination<Event> query(EventQuery query, Integer size);
}
