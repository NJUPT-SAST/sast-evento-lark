package fun.sast.evento.lark.api.v2.client;

import fun.sast.evento.lark.api.v2.value.V2;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/client/participation")
class ParticipationController {

    @PostMapping("/check-in/{eventId}")
    public Boolean checkIn(@PathVariable Integer eventId, @RequestParam String code) {
        return true;
    }

    @PostMapping("/subscribe/{eventId}")
    public Boolean subscribe(@PathVariable Integer eventId, @RequestParam Boolean subscribe) {
        return true;
    }

    @PostMapping("/subscribe/{larkDepartment}")
    public Boolean subscribe(@PathVariable String larkDepartment, @RequestParam Boolean subscribe) {
        return true;
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
