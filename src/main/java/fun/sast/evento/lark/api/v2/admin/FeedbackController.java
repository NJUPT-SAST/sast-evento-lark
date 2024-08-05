package fun.sast.evento.lark.api.v2.admin;

import fun.sast.evento.lark.api.v2.value.V2;
import fun.sast.evento.lark.domain.common.value.Pagination;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/admin/event")
class FeedbackController {

    @GetMapping("/{eventId}/feedback")
    public Pagination<V2.Feedback> getAllFeedbacks(@PathVariable Integer eventId,
                                                   @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                                   @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        return new Pagination<>(List.of(), 0, 0);
    }
}
