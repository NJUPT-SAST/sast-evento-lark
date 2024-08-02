package fun.sast.evento.lark.api.v2.client;

import fun.sast.evento.lark.api.v2.value.V2;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/client/feedback")
class FeedbackController {

    /**
     * @return 用户自己对活动的反馈，如果为null则未评论
     **/
    @GetMapping("/{eventId}")
    public V2.Feedback getFeedback(@PathVariable Integer eventId) {
        return null;
    }

    @PostMapping("/{eventId}")
    public Boolean createFeedback(@PathVariable Integer eventId,
                                  @RequestParam Integer rating,
                                  @RequestParam String content) {
        return true;
    }
}
