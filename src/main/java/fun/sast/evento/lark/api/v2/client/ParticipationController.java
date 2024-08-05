package fun.sast.evento.lark.api.v2.client;

import fun.sast.evento.lark.api.v2.value.V2;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/client/event")
class ParticipationController {

    @PostMapping("/{eventId}/check-in")
    public Boolean checkIn(@PathVariable Integer eventId, @RequestParam String code) {
        return true;
    }

    @PostMapping("/{eventId}/subscribe")
    public Boolean subscribe(@PathVariable Integer eventId, @RequestParam Boolean subscribe) {
        return true;
    }

    @PostMapping("/{larkDepartment}/subscribe")
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
