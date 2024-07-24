package fun.sast.evento.lark.api.v1.client;

import fun.sast.evento.lark.api.v1.value.V1;
import fun.sast.evento.lark.domain.common.value.Pagination;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vi/feedback")
class FeedbackController {

    @GetMapping("/event")
    public V1.Feedbacks getFeedback(@RequestParam Integer eventId) {
        return null;
    }

    @GetMapping("/num")
    public V1.Page<V1.FeedbackNum> getFeedbackEvents(
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        return null;
    }


    @PostMapping("/info")
    public Boolean addFeedback(@RequestParam(required = false) String content,
                               @RequestParam Double score,
                               @RequestParam Integer eventId) {
        return true;
    }

    @GetMapping("/user/list")
    public List<V1.Feedback> getListByUserId() {
        return null;
    }

    @GetMapping("/user/info")
    public V1.Feedback getUserFeedback(@RequestParam Integer eventId) {
        return null;
    }

    @PatchMapping("/info")
    public Boolean patchFeedback(@RequestParam(required = false) String content,
                                 @RequestParam(required = false) Double score,
                                 @RequestParam Integer feedbackId) {
        return true;
    }

    @DeleteMapping("/info")
    public Boolean deleteFeedback(@RequestParam Integer feedbackId) {
        return true;
    }

    @GetMapping("/list")
    public List<V1.Feedback> getListByEventId(@RequestParam Integer eventId) {
        return List.of();
    }

}
