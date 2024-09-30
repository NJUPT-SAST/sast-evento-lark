package fun.sast.evento.lark.domain.subscription.service;

import fun.sast.evento.lark.domain.common.value.EventState;

import java.time.LocalDateTime;

public interface MessageService {

    void schedule(Long eventId, EventState state, LocalDateTime time);
}
