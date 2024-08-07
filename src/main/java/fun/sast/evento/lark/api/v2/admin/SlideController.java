package fun.sast.evento.lark.api.v2.admin;

import fun.sast.evento.lark.api.v2.value.V2;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/admin/event")
public class SlideController {

    @PostMapping("/{eventId}/slide")
    public V2.Slide uploadSlide(@PathVariable Long eventId, @RequestParam String url, @RequestParam String link) {
        return null;
    }

    @DeleteMapping("/{eventId}/slide/{slideId}")
    public Boolean deleteSlide(@PathVariable Long eventId, @PathVariable Long slideId) {
        return true;
    }

    @PostMapping("/slide")
    public V2.Slide uploadSlide(@RequestParam String url, @RequestParam String link) {
        return null;
    }

    @DeleteMapping("/slide/{slideId}")
    public Boolean deleteSlide(@PathVariable Long slideId) {
        return true;
    }
}
