package fun.sast.evento.lark.api.v2.client;

import fun.sast.evento.lark.domain.event.service.SubscriptionService;
import fun.sast.evento.lark.domain.link.value.User;
import fun.sast.evento.lark.domain.subscription.service.impl.EventStateUpdatePublishService;
import fun.sast.evento.lark.infrastructure.utils.JsonUtils;
import jakarta.annotation.Resource;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

public class SubscriptionHandler implements WebSocketHandler {

    @Resource
    EventStateUpdatePublishService eventStateUpdatePublishService;
    @Resource
    SubscriptionService subscriptionService;

    @NotNull
    @Override
    public Mono<Void> handle(@NotNull WebSocketSession session) {
        User user = (User) session.getAttributes().get("user");
        return session.send(eventStateUpdatePublishService.subscribe()
                .filter(event -> subscriptionService.isSubscribed(event.eventId(), user.getUserId()))
                .map(event -> session.textMessage(JsonUtils.toJson(event))));
    }
}
