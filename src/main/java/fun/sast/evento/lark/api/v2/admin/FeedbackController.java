package fun.sast.evento.lark.api.v2.admin;

import fun.sast.evento.lark.api.security.Permission;
import fun.sast.evento.lark.api.security.RequirePermission;
import fun.sast.evento.lark.api.v2.value.V2;
import fun.sast.evento.lark.domain.common.value.Pagination;
import fun.sast.evento.lark.domain.event.entity.Feedback;
import fun.sast.evento.lark.domain.event.service.FeedbackService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController(value = "adminFeedbackController")
@RequestMapping("/api/v2/admin/event")
class FeedbackController {

    @Resource
    FeedbackService feedbackService;

    @GetMapping("/{eventId}/feedback")
    @RequirePermission(Permission.MANAGER)
    Pagination<V2.Feedback> getAllFeedbacks(@PathVariable Long eventId,
                                            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        Pagination<Feedback> feedbacks = feedbackService.list(eventId, page, size);
        return new Pagination<>(
                feedbacks.elements().stream().map(feedbackService::mapToV2).toList(),
                feedbacks.current(),
                feedbacks.total());
    }
}
