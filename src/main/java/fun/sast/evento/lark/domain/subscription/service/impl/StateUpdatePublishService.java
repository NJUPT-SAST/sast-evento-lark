package fun.sast.evento.lark.domain.subscription.service.impl;

import fun.sast.evento.lark.domain.subscription.event.StateUpdateEvent;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;

public class StateUpdatePublishService extends RedissonPublishService<StateUpdateEvent> {

    public StateUpdatePublishService(RedissonClient redissonClient, @Value("${app.event-bus.topic}") String topic) {
        super(redissonClient, topic);
    }

    @Override
    Class<StateUpdateEvent> getClazz() {
        return StateUpdateEvent.class;
    }
}
