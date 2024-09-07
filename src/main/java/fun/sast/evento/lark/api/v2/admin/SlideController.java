package fun.sast.evento.lark.api.v2.admin;

import fun.sast.evento.lark.api.v2.value.V2;
import fun.sast.evento.lark.domain.event.entity.Slide;
import fun.sast.evento.lark.domain.event.service.SlideService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/admin/event")
class SlideController {

    @Resource
    SlideService slideService;

    @PostMapping("/{eventId}/slide")
    V2.Slide uploadSlide(@PathVariable Long eventId, @RequestParam String url, @RequestParam String link) {
        Slide slide = slideService.uploadSlide(eventId, url, link);
        return slideService.mapToV2Slide(slide);
    }

    @DeleteMapping("/{eventId}/slide/{slideId}")
    Boolean deleteSlide(@PathVariable Long eventId, @PathVariable Long slideId) {
        return slideService.deleteSlide(eventId, slideId);
    }

    @PostMapping("/slide")
    V2.Slide uploadSlide(@RequestParam String url, @RequestParam String link) {
        Slide slide = slideService.uploadSlide(url, link);
        return slideService.mapToV2Slide(slide);
    }

    @DeleteMapping("/slide/{slideId}")
    Boolean deleteSlide(@PathVariable Long slideId) {
        return slideService.deleteSlide(slideId);
    }
}
