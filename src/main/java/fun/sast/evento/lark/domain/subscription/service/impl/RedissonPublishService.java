package fun.sast.evento.lark.domain.subscription.service.impl;

import fun.sast.evento.lark.domain.subscription.service.PublishService;
import jakarta.annotation.PreDestroy;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.util.concurrent.atomic.AtomicReference;

public abstract class RedissonPublishService<T> implements PublishService<T> {

    private final RTopic rTopic;
    private final AtomicReference<FluxSink<T>> sink = new AtomicReference<>();
    private final Flux<T> flux = Flux.create(sink::set).share();
    private final int listenerId;

    protected RedissonPublishService(RedissonClient redissonClient, String topic) {
        rTopic = redissonClient.getTopic(topic);
        listenerId = rTopic.addListener(getClazz(), (entry, value) -> {
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

    @PreDestroy
    public void cleanup() {
        FluxSink<T> currentSink = sink.getAndSet(null);
        if (currentSink != null) {
            currentSink.complete();
        }
        rTopic.removeListener(listenerId);
    }
}
