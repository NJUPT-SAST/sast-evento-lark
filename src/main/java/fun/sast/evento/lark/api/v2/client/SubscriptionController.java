package fun.sast.evento.lark.api.v2.client;

import fun.sast.evento.lark.domain.event.service.SubscriptionService;
import fun.sast.evento.lark.domain.subscription.event.EventStateUpdateEvent;
import fun.sast.evento.lark.infrastructure.auth.JWTInterceptor;
import jakarta.annotation.Resource;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/v2/client/event")
class SubscriptionController {

    @Resource
    SubscriptionService subscriptionService;

    @PostMapping("/{eventId}/check-in")
    Boolean checkIn(@PathVariable Long eventId, @RequestParam String code) {
        return subscriptionService.checkIn(eventId, JWTInterceptor.userHolder.get().getUserId(), code);
    }

    @PostMapping("/{identifier}/subscribe")
    Boolean subscribe(@PathVariable String identifier, @RequestParam Boolean subscribe) {
        if (identifier.matches("\\d+")) {
            Long eventId = Long.parseLong(identifier);
            // 处理活动订阅
            return subscriptionService.subscribeEvent(eventId, JWTInterceptor.userHolder.get().getUserId(), subscribe);
        } else {
            // 处理部门订阅
            return subscriptionService.subscribeDepartment(identifier, JWTInterceptor.userHolder.get().getUserId(), subscribe);
        }
    }

    @GetMapping("/{identifier}/is-subscribed")
    Boolean isSubscribed(@PathVariable String identifier) {
        if (identifier.matches("\\d+")) {
            Long eventId = Long.parseLong(identifier);
            return subscriptionService.isSubscribed(eventId, JWTInterceptor.userHolder.get().getUserId());
        } else {
            return subscriptionService.isSubscribed(identifier, JWTInterceptor.userHolder.get().getUserId());
        }
    }

    @GetMapping("/subscription")
    Flux<ServerSentEvent<EventStateUpdateEvent>> subscription() {
        return subscriptionService.subscription(JWTInterceptor.userHolder.get().getUserId());
    }
}
