package fun.sast.evento.lark.api.v2.client;

import fun.sast.evento.lark.domain.event.service.SubscriptionService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/client/event")
class SubscriptionController {

    @Resource
    SubscriptionService subscriptionService;

    @PostMapping("/{eventId}/check-in")
    Boolean checkIn(@PathVariable Long eventId, @RequestParam String code) {
        return subscriptionService.checkIn(eventId, code);
    }

    @PostMapping("/{identifier}/subscribe")
    Boolean subscribe(@PathVariable String identifier, @RequestParam Boolean subscribe) {
        if (identifier.matches("\\d+")) {
            Long eventId = Long.parseLong(identifier);
            // 处理活动订阅
            return subscriptionService.subscribeEvent(eventId, subscribe);
        } else {
            // 处理部门订阅
            return subscriptionService.subscribeDepartment(identifier, subscribe);
        }
    }
}
