package fun.sast.evento.lark.domain.event.service;

import fun.sast.evento.lark.api.v2.value.V2;
import fun.sast.evento.lark.domain.event.entity.Slide;

import java.util.List;

public interface SlideService {

    Slide uploadSlide(Long eventId, String url, String link);

    Boolean deleteSlide(Long eventId, Long slideId);

    Slide uploadSlide(String url, String link);

    Boolean deleteSlide(Long slideId);

    List<Slide> getSlides();

    List<Slide> getSlides(Long eventId);

    V2.Slide mapToV2Slide(Slide slide);
}
