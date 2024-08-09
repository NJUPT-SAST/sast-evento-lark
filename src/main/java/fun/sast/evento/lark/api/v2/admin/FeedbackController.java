package fun.sast.evento.lark.api.v2.admin;

import fun.sast.evento.lark.api.v2.value.V2;
import fun.sast.evento.lark.domain.common.value.Pagination;
import fun.sast.evento.lark.domain.event.service.FeedbackService;
import fun.sast.evento.lark.domain.event.entity.Feedback;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/admin/event")
class FeedbackController {

    @Resource
    private FeedbackService feedbackService;

    @GetMapping("/{eventId}/feedback")
    public Pagination<V2.Feedback> getAllFeedbacks(@PathVariable Long eventId,
                                                   @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                                   @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        Pagination<Feedback> feedbacks = feedbackService.getAllFeedbacks(eventId, page, size);
        return new Pagination<>(
                feedbacks.elements().stream().map(feedbackService::mapToV2Feedback).toList(),
                feedbacks.current(),
                feedbacks.total());
    }
}
