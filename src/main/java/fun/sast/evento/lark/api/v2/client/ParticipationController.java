package fun.sast.evento.lark.api.v2.client;

import fun.sast.evento.lark.api.v2.value.V2;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/client/event")
class ParticipationController {

    @PostMapping("/{eventId}/check-in")
    public Boolean checkIn(@PathVariable Long eventId, @RequestParam String code) {
        return null;
    }

    @PostMapping("/{identifier}/subscribe")
    public Boolean subscribe(@PathVariable String identifier, @RequestParam Boolean subscribe) {
        if (identifier.matches("\\d+")) {
            Long eventId = Long.parseLong(identifier);
            // 处理活动订阅
        } else {
            // 处理部门订阅
        }
        return null;
    }

    @GetMapping("/participated")
    public List<V2.Event> getParticipatedEvents() {
        return List.of();
    }

    @GetMapping("/subscribed")
    public List<V2.Event> getSubscribedEvents() {
        return List.of();
    }
}
