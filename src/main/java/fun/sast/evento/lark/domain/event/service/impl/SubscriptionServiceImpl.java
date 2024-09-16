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
            subscription.setLinkId(getLinkId());
            subscription.setCheckedIn(true);
            return subscriptionMapper.insert(subscription) > 0;
        } else {
            subscription.setCheckedIn(true);
            return subscriptionMapper.updateById(subscription) > 0;
        }
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
            subscription.setLinkId(getLinkId());
            subscription.setSubscribed(subscribe);
            return subscriptionMapper.insert(subscription) > 0;
        } else {
            subscription.setSubscribed(subscribe);
            return subscriptionMapper.updateById(subscription) > 0;
        }
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
            departmentSubscription.setLinkId(getLinkId());
            return departmentSubscriptionMapper.insert(departmentSubscription) > 0;
        } else if (!subscribe && departmentSubscription != null) {
            return departmentSubscriptionMapper.deleteById(departmentSubscription.getId()) > 0;
        }
        return false;
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
    public Boolean isSubscribed(String departmentId) {
        QueryWrapper<DepartmentSubscription> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("department_id", departmentId);
        queryWrapper.eq("link_id", getLinkId());
        DepartmentSubscription departmentSubscription = departmentSubscriptionMapper.selectOne(queryWrapper);
        return departmentSubscription != null;
    }

    @Override
    public Boolean delete(Long eventId) {
        QueryWrapper<Subscription> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("event_id", eventId);
        queryWrapper.eq("link_id", getLinkId());
        return subscriptionMapper.delete(queryWrapper) > 0;
    }

    private String getLinkId() {
        return JWTInterceptor.userHolder.get().getUserId();
    }
}
