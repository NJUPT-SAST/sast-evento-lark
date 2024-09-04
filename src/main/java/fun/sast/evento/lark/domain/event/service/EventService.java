package fun.sast.evento.lark.domain.event.service;

import fun.sast.evento.lark.api.v2.value.V2;
import fun.sast.evento.lark.domain.common.value.EventState;
import fun.sast.evento.lark.domain.common.value.Pagination;
import fun.sast.evento.lark.domain.event.entity.Event;
import fun.sast.evento.lark.domain.event.value.EventCreate;
import fun.sast.evento.lark.domain.event.value.EventQuery;
import fun.sast.evento.lark.domain.event.value.EventUpdate;

public interface EventService {
    Event create(EventCreate create);

    Event update(Long eventId, EventUpdate update);

    Boolean delete(Long eventId);

    Event cancel(Long eventId);

    Event get(Long eventId);

    Pagination<Event> list(Integer current, Integer size);

    Pagination<Event> query(EventQuery query, Integer current, Integer size);

    EventState calculateState(Event event);

    V2.Event mapToV2Event(Event event);
}
