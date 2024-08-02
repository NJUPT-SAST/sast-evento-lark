package fun.sast.evento.lark.api.v2.client;

import fun.sast.evento.lark.api.v2.value.V2;
import fun.sast.evento.lark.domain.common.value.Pagination;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/feedback")
public class FeedbackController {

    /**
     * @return 用户自己对活动的反馈，如果为null则未评论
     **/
    @GetMapping("/user")
    public V2.Feedback getFeedback(@RequestParam Integer eventId) {
        return null;
    }

    @PostMapping("/user")
    public Boolean createFeedback(@RequestParam Integer eventId,
                                  @RequestParam Integer rating,
                                  @RequestParam String content) {
        return true;
    }

    @GetMapping("/all")
    public Pagination<V2.Feedback> getAllFeedbacks(@RequestParam Integer eventId,
                                                   @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                                   @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        return new Pagination<>(List.of(), 0, 0);
    }
}
