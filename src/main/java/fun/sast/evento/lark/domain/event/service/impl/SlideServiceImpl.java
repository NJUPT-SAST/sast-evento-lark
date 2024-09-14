package fun.sast.evento.lark.domain.event.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import fun.sast.evento.lark.api.v2.value.V2;
import fun.sast.evento.lark.domain.event.entity.Slide;
import fun.sast.evento.lark.domain.event.service.EventService;
import fun.sast.evento.lark.domain.event.service.SlideService;
import fun.sast.evento.lark.infrastructure.error.BusinessException;
import fun.sast.evento.lark.infrastructure.error.ErrorEnum;
import fun.sast.evento.lark.infrastructure.oss.OSS;
import fun.sast.evento.lark.infrastructure.repository.SlideMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class SlideServiceImpl implements SlideService {

    @Resource
    private SlideMapper slideMapper;
    @Resource
    private OSS oss;
    @Resource
    private EventService eventService;

    @Override
    public Slide uploadSlide(Long eventId, MultipartFile file, String link) {
        if (eventService.get(eventId) == null) {
            throw new BusinessException(ErrorEnum.PARAM_ERROR, "Event not found");
        }
        String key = oss.upload(file);
        Slide slide = new Slide();
        slide.setEventId(eventId);
        slide.setKey(key);
        slide.setLink(link);
        slideMapper.insert(slide);
        return slide;
    }

    @Override
    public Boolean deleteSlide(Long eventId, Long slideId) {
        Slide slide = slideMapper.selectById(slideId);
        if (slide == null) {
            throw new BusinessException(ErrorEnum.PARAM_ERROR, "Slide not found");
        }
        if (!slide.getEventId().equals(eventId)) {
            throw new BusinessException(ErrorEnum.PARAM_ERROR, "Unexpected slide.");
        }
        oss.delete(slide.getKey());
        return slideMapper.deleteById(slideId) > 0;
    }

    @Override
    public Slide uploadSlide(MultipartFile file, String link) {
        String key = oss.upload(file);
        Slide slide = new Slide();
        slide.setKey(key);
        slide.setLink(link);
        slideMapper.insert(slide);
        return slide;
    }

    @Override
    public Boolean deleteSlide(Long slideId) {
        Slide slide = slideMapper.selectById(slideId);
        if (slide == null) {
            throw new BusinessException(ErrorEnum.PARAM_ERROR, "Slide not found");
        }
        oss.delete(slide.getKey());
        return slideMapper.deleteById(slideId) > 0;
    }

    @Override
    public List<Slide> getSlides() {
        QueryWrapper<Slide> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("event_id", null);
        return slideMapper.selectList(queryWrapper);
    }

    @Override
    public List<Slide> getSlides(Long eventId) {
        QueryWrapper<Slide> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("event_id", eventId);
        return slideMapper.selectList(queryWrapper);
    }

    @Override
    public V2.Slide mapToV2Slide(Slide slide) {
        return new V2.Slide(
                slide.getId(),
                slide.getEventId().toString(),
                oss.url(slide.getKey()).toString(),
                slide.getLink()
        );
    }
}
