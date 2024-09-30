package fun.sast.evento.lark.domain.subscription.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import fun.sast.evento.lark.domain.common.value.EventState;
import fun.sast.evento.lark.domain.subscription.entity.Message;
import fun.sast.evento.lark.domain.subscription.event.StateUpdateEvent;
import fun.sast.evento.lark.domain.subscription.service.MessageService;
import fun.sast.evento.lark.infrastructure.repository.MessageMapper;
import jakarta.annotation.Resource;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Service
public class MessageServiceImpl implements MessageService {

    @Resource
    private MessageMapper messageMapper;
    @Resource
    private TaskScheduler taskScheduler;
    private final Map<Long, ScheduledFuture<?>> futureMap = new ConcurrentHashMap<>();

    @Resource
    StateUpdatePublishService stateUpdatePublishService;

    // TODO: 启动时重加载信息

    @Override
    public void schedule(Long eventId, EventState state, LocalDateTime time) {
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("event_id", eventId);
        queryWrapper.eq("state", state);
        Message message = messageMapper.selectOne(queryWrapper);
        if (message == null) {
            message = new Message();
            message.setEventId(eventId);
            message.setState(state);
            message.setTime(time);
            messageMapper.insert(message);
        } else {
            message.setTime(time);
            messageMapper.updateById(message);
        }

        Long messageId = message.getId();

        futureMap.computeIfPresent(messageId, (k, v) -> {
            v.cancel(true);
            return null;
        });

        ScheduledFuture<?> future = taskScheduler.schedule(() -> {
            stateUpdatePublishService.publish(new StateUpdateEvent(eventId, state, time));
            messageMapper.deleteById(messageId);
        }, Instant.from(time));
        futureMap.put(messageId, future);
    }
}
