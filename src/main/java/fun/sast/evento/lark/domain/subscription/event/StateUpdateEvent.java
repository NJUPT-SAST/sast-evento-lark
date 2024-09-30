package fun.sast.evento.lark.domain.subscription.event;

import fun.sast.evento.lark.domain.common.value.EventState;

import java.time.LocalDateTime;

public record StateUpdateEvent(
        Long eventId,
        EventState state,
        LocalDateTime time
) {
}