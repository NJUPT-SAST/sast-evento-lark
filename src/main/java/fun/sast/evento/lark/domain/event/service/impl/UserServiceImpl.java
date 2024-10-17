package fun.sast.evento.lark.domain.event.service.impl;

import fun.feellmoose.model.UserInfo;
import fun.sast.evento.lark.api.security.Permission;
import fun.sast.evento.lark.api.v2.value.V2;
import fun.sast.evento.lark.domain.event.entity.User;
import fun.sast.evento.lark.domain.event.service.UserService;
import fun.sast.evento.lark.infrastructure.auth.JWTInterceptor;
import fun.sast.evento.lark.infrastructure.repository.UserMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public User getUser(UserInfo userInfo) {
        User user = userMapper.selectById(userInfo.getUserId());
        if (user == null) {
            user = new User();
            user.setUserId(userInfo.getUserId());
            user.setPermission(Permission.LOGIN.getNum());
        }
        user.setUserInfo(userInfo);
        return user;
    }

    @Override
    public V2.User getProfile() {
        User user = JWTInterceptor.userHolder.get();
        return new V2.User(
                user.getUserId(),
                user.getUserInfo().getEmail(),
                user.getUserInfo().getAvatar(),
                user.getUserInfo().getOrg(),
                user.getUserInfo().getNickname()
        );
    }
}
