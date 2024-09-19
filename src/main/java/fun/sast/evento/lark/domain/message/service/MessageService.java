package fun.sast.evento.lark.domain.message.service;

import fun.sast.evento.lark.domain.common.value.EventState;
import jakarta.websocket.Session;

import java.time.LocalDateTime;

public interface MessageService {

    void schedule(Long eventId, EventState state, LocalDateTime time);

    void connect(String linkId, Session session);

    void disconnect(String linkId);
}
