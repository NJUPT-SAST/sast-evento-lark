package fun.sast.evento.lark.api.v2.admin;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/admin/event")
class ParticipationController {

    @PostMapping("/{eventId}/code")
    public String generateCheckInCode(@PathVariable Long eventId) {
        return null;
    }

}
