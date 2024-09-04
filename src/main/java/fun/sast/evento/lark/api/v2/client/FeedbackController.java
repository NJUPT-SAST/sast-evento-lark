package fun.sast.evento.lark.api.v2.client;

import fun.sast.evento.lark.api.v2.value.V2;
import fun.sast.evento.lark.domain.event.entity.Feedback;
import fun.sast.evento.lark.domain.event.service.FeedbackService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/client/event")
class FeedbackController {

    @Resource
    private FeedbackService feedbackService;

    /**
     * @return 用户自己对活动的反馈，如果为null则未评论
     **/
    @GetMapping("/{eventId}/feedback")
    public V2.Feedback getFeedback(@PathVariable Long eventId) {
        Feedback feedback = feedbackService.getFeedback(eventId);
        if(feedback == null){
            return null;
        }
        return feedbackService.mapToV2Feedback(feedback);
    }

    @PostMapping("/{eventId}/feedback")
    public V2.Feedback createFeedback(@PathVariable Long eventId,
                                      @RequestParam Integer rating,
                                      @RequestParam String content) {
        Feedback feedback = feedbackService.createFeedback(eventId, rating, content);
        return feedbackService.mapToV2Feedback(feedback);
    }
}
