package fun.sast.evento.lark.api.v2.client;

import fun.sast.evento.lark.api.v2.value.V2;
import fun.sast.evento.lark.domain.common.value.Pagination;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/client/event")
class EventController {

    @GetMapping("/active")
    public List<V2.Event> getActiveEvents() {
        return List.of();
    }

    @GetMapping("/latest")
    public List<V2.Event> getLatestEvents() {
        return List.of();
    }

    @GetMapping("/history")
    public Pagination<V2.Event> getHistoryEvents(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                                 @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        return new Pagination<>(List.of(), 0, 0);
    }

    @GetMapping("/{eventId}/attachment")
    public List<V2.Attachment> getAttachments(@PathVariable Integer eventId) {
        return List.of();
    }
}
