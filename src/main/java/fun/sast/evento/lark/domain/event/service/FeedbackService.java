package fun.sast.evento.lark.domain.event.service;

import fun.sast.evento.lark.domain.common.value.Pagination;
import fun.sast.evento.lark.domain.event.value.Feedback;

public interface FeedbackService {

    Feedback getFeedback(Long eventId);

    Feedback createFeedback(Long eventId, Integer rating, String content);

    Pagination<Feedback> getAllFeedbacks(Long eventId, Integer page, Integer size);
}
