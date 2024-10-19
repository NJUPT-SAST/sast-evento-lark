package fun.sast.evento.lark;

import fun.sast.evento.lark.api.security.Permission;
import fun.sast.evento.lark.domain.event.entity.User;
import fun.sast.evento.lark.infrastructure.auth.JWTService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;

@SpringBootTest
public class UserTest {

    @Autowired
    JWTService jwtService;
    @Autowired
    CacheManager cacheManager;

    @Test
    void generateUser(){
        User user = new User();
        user.setUserId("B00000000");
        user.setPermission(Permission.LOGIN.getNum());

        String token = jwtService.generate(new JWTService.Payload<>(user));
        System.out.println(token);

        cacheManager.getCache("user").put("B00000000", token);
    }

    @Test
    void generateAdmin(){
        User user = new User();
        user.setUserId("B00000001");
        user.setPermission(Permission.ADMIN.getNum());

        String token = jwtService.generate(new JWTService.Payload<>(user));
        System.out.println(token);

        cacheManager.getCache("user").put("B00000001", token);
    }
}
