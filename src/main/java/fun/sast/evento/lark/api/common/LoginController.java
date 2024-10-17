package fun.sast.evento.lark.api.common;

import fun.feellmoose.model.UserInfo;
import fun.feellmoose.model.response.data.AccessToken;
import fun.feellmoose.service.SastLinkService;
import fun.sast.evento.lark.domain.event.entity.User;
import fun.sast.evento.lark.domain.event.service.UserService;
import fun.sast.evento.lark.infrastructure.auth.JWTService;
import fun.sast.evento.lark.infrastructure.cache.Cache;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/login")
@AllArgsConstructor
class LoginController {

    SastLinkService sastLinkService;
    UserService userService;
    Cache cache;
    JWTService jwtService;

    record Login(String token, User user) {
    }

    @PostMapping("/link")
    Login link(@RequestParam String code, @RequestParam Integer type) {
        AccessToken accessToken = sastLinkService.accessToken(code);
        UserInfo userInfo = sastLinkService.user(accessToken.getAccessToken());
        User user = userService.getUser(userInfo);
        cache.set(user.getUserId(), user);
        String token = jwtService.generate(new JWTService.Payload<>(user));
        return new Login(token, user);
    }

    @PostMapping("/refresh-token")
    String refreshToken(@RequestParam String token) {
        return sastLinkService.refreshToken(token).getAccessToken();
    }
}
