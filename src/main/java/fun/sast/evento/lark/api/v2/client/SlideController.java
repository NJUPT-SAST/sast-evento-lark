package fun.sast.evento.lark.api.v2.client;

import fun.sast.evento.lark.api.v2.value.V2;
import fun.sast.evento.lark.domain.event.entity.Slide;
import fun.sast.evento.lark.domain.event.service.SlideService;
import jakarta.annotation.Resource;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v2/client/event")
class SlideController {

    @Resource
    SlideService slideService;

    @GetMapping("/slide")
    @Cacheable(cacheNames = "slides", key = "root")
    public List<V2.Slide> getSlides() {
        List<Slide> slides = slideService.getSlides();
        return slides.stream().map(slideService::mapToV2Slide).toList();
    }

    @GetMapping("/{eventId}/slide")
    @Cacheable(cacheNames = "slides", key = "#eventId")
    public List<V2.Slide> getSlides(@PathVariable Long eventId) {
        List<Slide> slides = slideService.getSlides(eventId);
        return slides.stream().map(slideService::mapToV2Slide).toList();
    }
}
