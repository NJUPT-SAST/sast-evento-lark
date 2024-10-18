package fun.sast.evento.lark.domain.event.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import fun.sast.evento.lark.domain.event.entity.DepartmentSubscription;
import fun.sast.evento.lark.domain.event.entity.Subscription;
import fun.sast.evento.lark.domain.event.service.SubscriptionService;
import fun.sast.evento.lark.domain.subscription.event.EventStateUpdateEvent;
import fun.sast.evento.lark.domain.subscription.service.impl.EventStateUpdatePublishService;
import fun.sast.evento.lark.infrastructure.error.BusinessException;
import fun.sast.evento.lark.infrastructure.repository.DepartmentSubscriptionMapper;
import fun.sast.evento.lark.infrastructure.repository.SubscriptionMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
@Slf4j
public class SubscriptionServiceImpl implements SubscriptionService {

    @Resource
    private SubscriptionMapper subscriptionMapper;
    @Resource
    private DepartmentSubscriptionMapper departmentSubscriptionMapper;
    @Resource
    private CacheManager cacheManager;
    @Resource
    private EventStateUpdatePublishService eventStateUpdatePublishService;

    @Override
    public String generateCheckInCode(Long eventId) {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[5];
        random.nextBytes(bytes);
        String code = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes).substring(0, 5);
        Cache cache = cacheManager.getCache("checkin-code");
        if (cache == null) {
            log.error("Cache not found.");
            return null;
        }
        cache.put(code, eventId.toString());
        return code;
    }

    @Override
    public Boolean checkIn(Long eventId, String linkId, String code) {
        Cache cache = cacheManager.getCache("checkin-code");
        if (cache == null) {
            return false;
        }
        String matchedId = cache.get(code, String.class);
        if (matchedId == null) {
            throw new BusinessException("checkin-code not exists or has expired.");
        }
        if (!matchedId.equals(eventId.toString())) {
            throw new BusinessException("checkin-code not match.");
        }
        QueryWrapper<Subscription> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("event_id", eventId);
        queryWrapper.eq("link_id", linkId);
        Subscription subscription = subscriptionMapper.selectOne(queryWrapper);
        if (subscription == null) {
            subscription = new Subscription();
            subscription.setEventId(eventId);
            subscription.setLinkId(linkId);
            subscription.setCheckedIn(true);
            return subscriptionMapper.insert(subscription) > 0;
        } else {
            subscription.setCheckedIn(true);
            return subscriptionMapper.updateById(subscription) > 0;
        }
    }

    @Override
    public Boolean subscribeEvent(Long eventId, String linkId, Boolean subscribe) {
        QueryWrapper<Subscription> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("event_id", eventId);
        queryWrapper.eq("link_id", linkId);
        Subscription subscription = subscriptionMapper.selectOne(queryWrapper);
        if (subscription == null) {
            subscription = new Subscription();
            subscription.setEventId(eventId);
            subscription.setLinkId(linkId);
            subscription.setSubscribed(subscribe);
            return subscriptionMapper.insert(subscription) > 0;
        } else {
            subscription.setSubscribed(subscribe);
            return subscriptionMapper.updateById(subscription) > 0;
        }
    }

    @Override
    public Boolean subscribeDepartment(String departmentId, String linkId, Boolean subscribe) {
        QueryWrapper<DepartmentSubscription> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("department_id", departmentId);
        queryWrapper.eq("link_id", linkId);
        DepartmentSubscription departmentSubscription = departmentSubscriptionMapper.selectOne(queryWrapper);
        if (subscribe && departmentSubscription == null) {
            departmentSubscription = new DepartmentSubscription();
            departmentSubscription.setDepartmentId(departmentId);
            departmentSubscription.setLinkId(linkId);
            return departmentSubscriptionMapper.insert(departmentSubscription) > 0;
        } else if (!subscribe && departmentSubscription != null) {
            return departmentSubscriptionMapper.deleteById(departmentSubscription.getId()) > 0;
        }
        return false;
    }

    @Override
    public Boolean isSubscribed(Long eventId, String linkId) {
        QueryWrapper<Subscription> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("event_id", eventId);
        queryWrapper.eq("link_id", linkId);
        Subscription subscription = subscriptionMapper.selectOne(queryWrapper);
        return subscription != null && subscription.getSubscribed();
    }

    @Override
    public Boolean isCheckedIn(Long eventId, String linkId) {
        QueryWrapper<Subscription> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("event_id", eventId);
        queryWrapper.eq("link_id", linkId);
        Subscription subscription = subscriptionMapper.selectOne(queryWrapper);
        return subscription != null && subscription.getCheckedIn();
    }

    @Override
    public Boolean isSubscribed(String departmentId, String linkId) {
        QueryWrapper<DepartmentSubscription> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("department_id", departmentId);
        queryWrapper.eq("link_id", linkId);
        DepartmentSubscription departmentSubscription = departmentSubscriptionMapper.selectOne(queryWrapper);
        return departmentSubscription != null;
    }

    @Override
    public List<String> getSubscribedUsers(Long eventId) {
        QueryWrapper<Subscription> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("event_id", eventId);
        queryWrapper.eq("subscribed", true);
        List<Subscription> subscriptions = subscriptionMapper.selectList(queryWrapper);
        return subscriptions.stream().map(Subscription::getLinkId).toList();
    }

    @Override
    public List<String> getSubscribedUsers(String departmentId) {
        QueryWrapper<DepartmentSubscription> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("department_id", departmentId);
        List<DepartmentSubscription> subscriptions = departmentSubscriptionMapper.selectList(queryWrapper);
        return subscriptions.stream().map(DepartmentSubscription::getLinkId).toList();
    }

    @Override
    public Boolean delete(Long eventId) {
        QueryWrapper<Subscription> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("event_id", eventId);
        return subscriptionMapper.delete(queryWrapper) > 0;
    }

    @Override
    public Flux<ServerSentEvent<EventStateUpdateEvent>> subscription(String linkId) {
        return eventStateUpdatePublishService.subscribe()
                .filter(event -> isSubscribed(event.eventId(), linkId))
                .map(event -> ServerSentEvent.builder(event).build())
                .doOnError(e -> log.error("Subscription error", e));
    }
}
