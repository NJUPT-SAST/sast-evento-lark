package fun.sast.evento.lark.domain.event.service;

import fun.sast.evento.lark.domain.common.value.EventState;
import fun.sast.evento.lark.domain.common.value.Pagination;
import fun.sast.evento.lark.domain.event.entity.Event;
import fun.sast.evento.lark.domain.event.value.EventCreate;
import fun.sast.evento.lark.domain.event.value.EventUpdate;
import fun.sast.evento.lark.domain.event.value.EventQuery;

public interface EventService {
    Event create(EventCreate create);

    Event update(Integer id, EventUpdate update);

    Boolean delete(Integer id);

    Pagination<Event> list(Integer current, Integer size);

    Pagination<Event> query(EventQuery query, Integer current, Integer size);

    EventState calculateState(Event event);
}
