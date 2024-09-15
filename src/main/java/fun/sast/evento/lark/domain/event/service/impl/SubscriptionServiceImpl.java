package fun.sast.evento.lark.domain.event.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import fun.sast.evento.lark.domain.event.entity.DepartmentSubscription;
import fun.sast.evento.lark.domain.event.entity.Subscription;
import fun.sast.evento.lark.domain.event.service.SubscriptionService;
import fun.sast.evento.lark.infrastructure.auth.JWTInterceptor;
import fun.sast.evento.lark.infrastructure.cache.Cache;
import fun.sast.evento.lark.infrastructure.error.BusinessException;
import fun.sast.evento.lark.infrastructure.repository.DepartmentSubscriptionMapper;
import fun.sast.evento.lark.infrastructure.repository.SubscriptionMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    @Resource
    private SubscriptionMapper subscriptionMapper;
    @Resource
    private DepartmentSubscriptionMapper departmentSubscriptionMapper;
    @Resource
    private Cache cache;

    @Override
    public String generateCheckInCode(Long eventId) {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[5];
        random.nextBytes(bytes);
        String code = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes).substring(0, 5);
        cache.set("CHECKIN_CODE:" + code, eventId.toString()); // TODO: Expire time?
        return code;
    }

    @Override
    public Boolean checkIn(Long eventId, String code) {
        String matchedId = cache.get("CHECKIN_CODE:" + code, String.class);
        if (matchedId == null) {
            throw new BusinessException("checkin-code not exists or has expired.");
        }
        if (!matchedId.equals(eventId.toString())) {
            throw new BusinessException("checkin-code not match.");
        }
        QueryWrapper<Subscription> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("event_id", eventId);
        queryWrapper.eq("link_id", getLinkId());
        Subscription subscription = subscriptionMapper.selectOne(queryWrapper);
        if (subscription == null) {
            subscription = new Subscription();
            subscription.setEventId(eventId);
            subscription.setLinkId(JWTInterceptor.userHolder.get().getUserId());
            subscription.setCheckedIn(true);
            subscriptionMapper.insert(subscription);
        } else {
            subscription.setCheckedIn(true);
            subscriptionMapper.updateById(subscription);
        }
        return true;
    }

    @Override
    public Boolean subscribeEvent(Long eventId, Boolean subscribe) {
        QueryWrapper<Subscription> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("event_id", eventId);
        queryWrapper.eq("link_id", getLinkId());
        Subscription subscription = subscriptionMapper.selectOne(queryWrapper);
        if (subscription == null) {
            subscription = new Subscription();
            subscription.setEventId(eventId);
            subscription.setLinkId(JWTInterceptor.userHolder.get().getUserId());
            subscription.setSubscribed(subscribe);
            subscriptionMapper.insert(subscription);
        } else {
            subscription.setSubscribed(subscribe);
            subscriptionMapper.updateById(subscription);
        }
        return true;
    }

    @Override
    public Boolean subscribeDepartment(String departmentId, Boolean subscribe) {
        QueryWrapper<DepartmentSubscription> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("department_id", departmentId);
        queryWrapper.eq("link_id", getLinkId());
        DepartmentSubscription departmentSubscription = departmentSubscriptionMapper.selectOne(queryWrapper);
        if (subscribe && departmentSubscription == null) {
            departmentSubscription = new DepartmentSubscription();
            departmentSubscription.setDepartmentId(departmentId);
            departmentSubscription.setLinkId(JWTInterceptor.userHolder.get().getUserId());
            departmentSubscriptionMapper.insert(departmentSubscription);
        } else if (!subscribe && departmentSubscription != null) {
            departmentSubscriptionMapper.deleteById(departmentSubscription.getId());
        }
        return true;
    }

    @Override
    public Boolean isSubscribed(Long eventId) {
        QueryWrapper<Subscription> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("event_id", eventId);
        queryWrapper.eq("link_id", getLinkId());
        Subscription subscription = subscriptionMapper.selectOne(queryWrapper);
        return subscription != null && subscription.getSubscribed();
    }

    @Override
    public Boolean isCheckedIn(Long eventId) {
        QueryWrapper<Subscription> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("event_id", eventId);
        queryWrapper.eq("link_id", getLinkId());
        Subscription subscription = subscriptionMapper.selectOne(queryWrapper);
        return subscription != null && subscription.getCheckedIn();
    }

    @Override
    public List<Long> getParticipatedEvents() {
        QueryWrapper<Subscription> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("link_id", getLinkId());
        queryWrapper.eq("checked_in", true);
        return subscriptionMapper.selectList(queryWrapper)
                .stream()
                .map(Subscription::getEventId)
                .toList();
    }

    @Override
    public List<Long> getSubscribedEvents() {
        QueryWrapper<Subscription> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("link_id", getLinkId());
        queryWrapper.eq("subscribed", true);
        return subscriptionMapper.selectList(queryWrapper)
                .stream()
                .map(Subscription::getEventId)
                .toList();
    }

    private String getLinkId() {
        return JWTInterceptor.userHolder.get().getUserId();
    }
}
