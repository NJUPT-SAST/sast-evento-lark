package fun.sast.evento.lark.domain.event.service.impl;

import fun.feellmoose.model.UserInfo;
import fun.feellmoose.model.response.data.AccessToken;
import fun.feellmoose.service.SastLinkService;
import fun.sast.evento.lark.api.security.Permission;
import fun.sast.evento.lark.api.value.V2;
import fun.sast.evento.lark.domain.event.entity.User;
import fun.sast.evento.lark.domain.event.service.UserService;
import fun.sast.evento.lark.infrastructure.auth.JWTService;
import fun.sast.evento.lark.infrastructure.repository.UserMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Resource
    private SastLinkService sastLinkServiceWeb;
    @Resource
    private SastLinkService sastLinkServiceApp;
    @Resource
    private JWTService jwtService;
    @Resource
    private UserMapper userMapper;
    @Resource
    private CacheManager cacheManager;

    @Override
    public V2.Login login(String code, Integer type, String codeVerifier) {
        SastLinkService sastLinkService = type == 1 ? sastLinkServiceWeb : sastLinkServiceApp;
        AccessToken accessToken = sastLinkService.accessToken(code, codeVerifier);
        UserInfo userInfo = sastLinkService.user(accessToken.getAccessToken());
        User user = userMapper.selectById(userInfo.getUserId());
        if (user == null) {
            user = new User();
            user.setUserId(userInfo.getUserId());
            user.setPermission(Permission.LOGIN.getNum());
        }
        user.setUserInfo(userInfo);
        String token = jwtService.generate(new JWTService.Payload<>(user));
        Cache cache = cacheManager.getCache("user");
        if (cache == null) {
            log.error("user cache not found while logging in");
            throw new RuntimeException("user cache not found");
        }
        cache.put(user.getUserId(), token);
        return new V2.Login(token, mapToV2User(user));
    }

    @Override
    public Boolean assignManagerRole(String userId) {
        User user = new User();
        user.setUserId(userId);
        user.setPermission(Permission.MANAGER.getNum());
        userMapper.insertOrUpdate(user);
        Cache cache = cacheManager.getCache("user");
        if (cache == null) {
            log.error("user cache not found while assigning role");
            throw new RuntimeException("user cache not found");
        }
        cache.evictIfPresent(userId);
        return true;
    }

    @Override
    public V2.User mapToV2User(User user) {
        return new V2.User(
                user.getUserId(),
                user.getUserInfo().getEmail(),
                user.getUserInfo().getAvatar(),
                user.getUserInfo().getBadge(),
                user.getUserInfo().getBio(),
                user.getUserInfo().getDep(),
                user.getUserInfo().getHide(),
                user.getUserInfo().getLink(),
                user.getUserInfo().getNickname(),
                user.getUserInfo().getOrg()
        );
    }
}
