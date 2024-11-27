package fun.sast.evento.lark.domain.event.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import fun.feellmoose.model.UserInfo;
import fun.feellmoose.model.response.data.AccessToken;
import fun.feellmoose.model.response.data.RefreshToken;
import fun.feellmoose.service.SastLinkService;
import fun.sast.evento.lark.api.security.Permission;
import fun.sast.evento.lark.api.value.V2;
import fun.sast.evento.lark.domain.event.entity.User;
import fun.sast.evento.lark.domain.event.service.UserService;
import fun.sast.evento.lark.infrastructure.auth.JWTInterceptor;
import fun.sast.evento.lark.infrastructure.auth.JWTService;
import fun.sast.evento.lark.infrastructure.error.BusinessException;
import fun.sast.evento.lark.infrastructure.error.ErrorEnum;
import fun.sast.evento.lark.infrastructure.repository.UserMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Resource
    private SastLinkService sastLinkService;
    @Resource
    private JWTService jwtService;
    @Resource
    private UserMapper userMapper;
    @Value("${app.sast-link.app-redirect-uri}")
    private String appRedirectUri;
    @Value("${app.sast-link.web-redirect-uri}")
    private String webRedirectUri;

    @Override
    public V2.Login login(String code, Integer type, String codeVerifier) {
        AccessToken accessToken = sastLinkService.accessToken(code, type == 1 ? webRedirectUri : appRedirectUri, codeVerifier);
        UserInfo userInfo = sastLinkService.user(accessToken.getAccessToken());
        User user = userMapper.selectById(userInfo.getUserId());
        if (user == null) {
            user = new User();
            user.setUserId(userInfo.getUserId());
            user.setPermission(Permission.LOGIN.getNum());
        }
        user.setRefreshToken(accessToken.getRefreshToken());
        userMapper.insertOrUpdate(user);
        String token = jwtService.generate(new JWTService.Payload<>(user), 15);
        String refreshToken = jwtService.generate(new JWTService.Payload<>(user.getUserId()), 10080);
        return new V2.Login(token, refreshToken);
    }

    @Override
    public V2.Login refreshToken(String refreshToken) {
        String userId = jwtService.verify(refreshToken, new TypeReference<>() {
        });
        // User shouldn't be null
        User user = userMapper.selectById(userId);
        String token = jwtService.generate(new JWTService.Payload<>(user), 30);
        return new V2.Login(token, refreshToken);
    }

    @Override
    public V2.User getProfile() {
        try {
            User user = userMapper.selectById(JWTInterceptor.userHolder.get().getUserId());
            RefreshToken accessToken = sastLinkService.refreshToken(user.getRefreshToken());
            UserInfo userInfo = sastLinkService.user(accessToken.getAccessToken());
            user.setRefreshToken(accessToken.getRefreshToken());
            userMapper.insertOrUpdate(user);
            return new V2.User(
                    userInfo.getUserId(),
                    userInfo.getEmail(),
                    userInfo.getAvatar(),
                    userInfo.getBadge(),
                    userInfo.getBio(),
                    userInfo.getDep(),
                    userInfo.getHide(),
                    userInfo.getLink(),
                    userInfo.getNickname(),
                    userInfo.getOrg(),
                    user.getPermission()
            );
        } catch (Exception e) {
            throw new BusinessException(ErrorEnum.TOKEN_EXPIRED);
        }
    }

    @Override
    public Boolean assignManagerRole(String userId) {
        User user = new User();
        user.setUserId(userId);
        user.setPermission(Permission.MANAGER.getNum());
        userMapper.insertOrUpdate(user);
        return true;
    }
}
