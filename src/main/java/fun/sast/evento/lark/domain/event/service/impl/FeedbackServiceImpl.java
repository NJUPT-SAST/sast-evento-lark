package fun.sast.evento.lark.domain.event.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import fun.sast.evento.lark.api.value.V2;
import fun.sast.evento.lark.domain.common.value.Pagination;
import fun.sast.evento.lark.domain.event.entity.Event;
import fun.sast.evento.lark.domain.event.entity.Feedback;
import fun.sast.evento.lark.domain.event.service.EventService;
import fun.sast.evento.lark.domain.event.service.FeedbackService;
import fun.sast.evento.lark.infrastructure.auth.JWTInterceptor;
import fun.sast.evento.lark.infrastructure.error.BusinessException;
import fun.sast.evento.lark.infrastructure.error.ErrorEnum;
import fun.sast.evento.lark.infrastructure.repository.FeedbackMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    @Resource
    private FeedbackMapper feedbackMapper;
    @Resource
    private EventService eventService;

    @Override
    public Feedback getFeedback(Long eventId) {
        QueryWrapper<Feedback> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("event_id", eventId);
        queryWrapper.eq("link_id", JWTInterceptor.userHolder.get().getUserId());
        return feedbackMapper.selectOne(queryWrapper);
    }

    @Override
    public Feedback createFeedback(Long eventId, Integer rating, String content) {
        if (getFeedback(eventId) != null) {
            throw new BusinessException(ErrorEnum.FEEDBACK_ALREADY_GIVEN);
        }
        // check time
        Event event = eventService.get(eventId);
        if (event == null) {
            throw new BusinessException(ErrorEnum.PARAM_ERROR, "Event not found");
        }
        if (LocalDateTime.now().isBefore(event.getEnd())) {
            throw new BusinessException(ErrorEnum.PARAM_ERROR, "The event has not ended yet");
        }
        Feedback feedback = new Feedback();
        feedback.setEventId(eventId);
        feedback.setLinkId(JWTInterceptor.userHolder.get().getUserId());
        feedback.setRating(rating);
        feedback.setContent(content);
        feedbackMapper.insert(feedback);
        return feedback;
    }

    @Override
    public Pagination<Feedback> list(Long eventId, Integer current, Integer size) {
        QueryWrapper<Feedback> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("event_id", eventId);
        return new Pagination<>(feedbackMapper.selectList(new Page<>(current, size), queryWrapper), current, size);
    }

    @Override
    public V2.Feedback mapToV2(Feedback feedback) {
        return new V2.Feedback(
                feedback.getId(),
                feedback.getEventId(),
                feedback.getLinkId(),
                feedback.getRating(),
                feedback.getContent()
        );
    }
}
