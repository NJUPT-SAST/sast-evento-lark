package fun.sast.evento.lark.domain.subscription.service;

import reactor.core.publisher.Flux;

public interface PublishService<T> {

    void publish(T t);

    Flux<T> subscribe();
}
