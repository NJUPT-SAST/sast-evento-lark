package fun.sast.evento.lark.domain.subscription.service.impl;

import fun.sast.evento.lark.domain.subscription.service.PublishService;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

public abstract class RedissonPublishService<T> implements PublishService<T> {

    private final RTopic rTopic;
    private final AtomicReference<FluxSink<T>> sink = new AtomicReference<>();
    private final Flux<T> flux = Flux.create(sink::set).share();

    protected RedissonPublishService(RedissonClient redissonClient, String topic) {
        rTopic = redissonClient.getTopic(topic);
        rTopic.addListener(getClazz(), (entry, value) -> {
            var current = sink.get();
            if (current != null) {
                current.next(value);
            }
        });
    }

    abstract Class<T> getClazz();

    @Override
    public void publish(T t) {
        rTopic.publish(t);
    }

    @Override
    public Flux<T> subscribe() {
        return flux;
    }
}
