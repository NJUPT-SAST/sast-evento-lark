package fun.sast.evento.lark.api.v2.admin;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/admin/participation")
class ParticipationController {

    @PostMapping("/code/{eventId}")
    public String generateCheckInCode(@PathVariable Integer eventId) {
        return null;
    }

}
