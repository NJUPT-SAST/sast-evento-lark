package fun.sast.evento.lark.api.v2.admin;

import fun.sast.evento.lark.api.security.Permission;
import fun.sast.evento.lark.api.security.RequirePermission;
import fun.sast.evento.lark.domain.event.service.SubscriptionService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "adminSubscriptionController")
@RequestMapping("/api/v2/admin/event")
class SubscriptionController {

    @Resource
    SubscriptionService subscriptionService;

    @PostMapping("/{eventId}/code")
    @RequirePermission(Permission.MANAGER)
    String generateCheckInCode(@PathVariable Long eventId) {
        return subscriptionService.generateCheckInCode(eventId);
    }

}
