package fun.sast.evento.lark.domain.subscription.service.impl;

import fun.sast.evento.lark.domain.subscription.event.EventStateUpdateEvent;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;

public class EventStateUpdatePublishService extends RedissonPublishService<EventStateUpdateEvent> {

    public EventStateUpdatePublishService(RedissonClient redissonClient, @Value("${app.event-bus.topic}") String topic) {
        super(redissonClient, topic);
    }

    @Override
    Class<EventStateUpdateEvent> getClazz() {
        return EventStateUpdateEvent.class;
    }
}
