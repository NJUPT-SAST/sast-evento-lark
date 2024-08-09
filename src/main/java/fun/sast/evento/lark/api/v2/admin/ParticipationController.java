package fun.sast.evento.lark.api.v2.admin;

import fun.sast.evento.lark.domain.event.service.ParticipationService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/admin/event")
class ParticipationController {

    @Resource
    private ParticipationService participationService;

    @PostMapping("/{eventId}/code")
    public String generateCheckInCode(@PathVariable Long eventId) {
        return participationService.generateCheckInCode(eventId);
    }

}
