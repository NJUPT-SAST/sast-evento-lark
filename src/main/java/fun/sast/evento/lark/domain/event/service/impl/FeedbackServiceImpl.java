package fun.sast.evento.lark.domain.event.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import fun.sast.evento.lark.api.v2.value.V2;
import fun.sast.evento.lark.domain.common.value.Pagination;
import fun.sast.evento.lark.domain.event.entity.Feedback;
import fun.sast.evento.lark.domain.event.service.FeedbackService;
import fun.sast.evento.lark.infrastructure.auth.JWTInterceptor;
import fun.sast.evento.lark.infrastructure.error.BusinessException;
import fun.sast.evento.lark.infrastructure.repository.FeedbackMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    @Resource
    private FeedbackMapper feedbackMapper;

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
            throw new BusinessException("You have already given feedback for this event");
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
