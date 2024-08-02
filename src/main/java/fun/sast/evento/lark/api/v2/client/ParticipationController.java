package fun.sast.evento.lark.api.v2.client;

import fun.sast.evento.lark.api.v2.value.V2;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/user")
public class ParticipationController {

    @PostMapping("/checkin")
    public Boolean checkin(@RequestParam Integer eventId) {
        return true;
    }

    @PostMapping("/subscribe")
    public Boolean subscribe(@RequestParam Integer eventId, @RequestParam Boolean subscribe) {
        return true;
    }

    @PostMapping("/subscribe")
    public Boolean subscribe(@RequestParam String larkDepartment, @RequestParam Boolean subscribe) {
        return true;
    }

    @GetMapping("/participated")
    public List<V2.Event> getParticipatedEvents() {
        return List.of();
    }

    @PostMapping("/subscribed")
    public List<V2.Event> getSubscribedEvents() {
        return List.of();
    }
}
