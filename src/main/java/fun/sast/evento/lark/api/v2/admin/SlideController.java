package fun.sast.evento.lark.api.v2.admin;

import fun.sast.evento.lark.api.v2.value.V2;
import fun.sast.evento.lark.domain.event.entity.Slide;
import fun.sast.evento.lark.domain.event.service.SlideService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController(value = "adminSlideController")
@RequestMapping("/api/v2/admin/event")
class SlideController {

    @Resource
    SlideService slideService;

    @PostMapping("/{eventId}/slide")
    V2.Slide uploadSlide(@PathVariable Long eventId, @RequestParam MultipartFile file, @RequestParam String link) {
        Slide slide = slideService.uploadSlide(eventId, file, link);
        return slideService.mapToV2Slide(slide);
    }

    @DeleteMapping("/{eventId}/slide/{slideId}")
    Boolean deleteSlide(@PathVariable Long eventId, @PathVariable Long slideId) {
        return slideService.deleteSlide(eventId, slideId);
    }

    @PostMapping("/slide")
    V2.Slide uploadSlide(@RequestParam MultipartFile file, @RequestParam String link) {
        Slide slide = slideService.uploadSlide(file, link);
        return slideService.mapToV2Slide(slide);
    }

    @DeleteMapping("/slide/{slideId}")
    Boolean deleteSlide(@PathVariable Long slideId) {
        return slideService.deleteSlide(slideId);
    }
}
