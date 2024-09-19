package fun.sast.evento.lark.domain.message.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import fun.sast.evento.lark.api.v2.value.V2;
import fun.sast.evento.lark.domain.common.value.EventState;
import fun.sast.evento.lark.domain.event.service.SubscriptionService;
import fun.sast.evento.lark.domain.message.entity.Message;
import fun.sast.evento.lark.domain.message.entity.UnsentMessage;
import fun.sast.evento.lark.domain.message.service.MessageService;
import fun.sast.evento.lark.infrastructure.repository.MessageMapper;
import fun.sast.evento.lark.infrastructure.repository.UnsentMessageMapper;
import jakarta.annotation.Resource;
import jakarta.websocket.Session;
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
    private UnsentMessageMapper unsentMessageMapper;
    @Resource
    private TaskScheduler taskScheduler;
    @Resource
    private SubscriptionService subscriptionService;
    private final Map<Long, ScheduledFuture<?>> futureMap = new ConcurrentHashMap<>();
    private final Map<String, Session> sessionMap = new ConcurrentHashMap<>();

    // TODO: 启动时重加载信息
    // TODO: 单个用户保存多个session

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

        futureMap.computeIfPresent(message.getId(), (k, v) -> {
            v.cancel(true);
            return null;
        });

        Message finalMessage = message;
        ScheduledFuture<?> future = taskScheduler.schedule(() -> {
            if (finalMessage.getState() == EventState.COMPLETED) {
                unsentMessageMapper.delete(new QueryWrapper<UnsentMessage>().eq("event_id", finalMessage.getEventId()));
                messageMapper.deleteById(new QueryWrapper<Message>().eq("event_id", finalMessage.getEventId()));
                return;
            }
            subscriptionService.getSubscribedUsers(eventId).forEach(user -> send(finalMessage, user));
        }, Instant.from(time));
        futureMap.put(message.getId(), future);
    }

    private void send(Message message, String linkId) {
        Session session = sessionMap.get(linkId);
        if (session == null) {
            UnsentMessage unsentMessage = new UnsentMessage();
            unsentMessage.setMessageId(message.getId());
            unsentMessage.setLinkId(linkId);
            unsentMessageMapper.insert(unsentMessage);
        } else {
            session.getAsyncRemote().sendObject(new V2.StateUpdateEvent(message.getEventId()));
        }
    }

    @Override
    public void connect(String linkId, Session session) {
        sessionMap.put(linkId, session);
        unsentMessageMapper.selectList(new QueryWrapper<UnsentMessage>().eq("link_id", linkId)).forEach(unsentMessage -> {
            Message message = messageMapper.selectById(unsentMessage.getMessageId());
            send(message, linkId);
        });
        unsentMessageMapper.delete(new QueryWrapper<UnsentMessage>().eq("link_id", linkId));
    }

    @Override
    public void disconnect(String linkId) {
        sessionMap.remove(linkId);
    }
}
