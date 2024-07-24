package fun.sast.evento.lark.api.common;

import fun.feellmoose.model.response.data.AccessToken;
import fun.feellmoose.service.SastLinkService;
import fun.sast.evento.lark.domain.link.value.User;
import fun.sast.evento.lark.infrastructure.auth.JWTService;
import fun.sast.evento.lark.infrastructure.cache.Cache;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/login")
@AllArgsConstructor
@ConditionalOnBean(JWTService.class)
class LoginController {

    SastLinkService sastLinkService;
    Cache cache;
    JWTService jwtService;

    record Login(String token, User user) {
    }

    @PostMapping("/link")
    Login link(@RequestParam String code, @RequestParam Integer type) {
        AccessToken accessToken = sastLinkService.accessToken(code);
        User user = (User) sastLinkService.user(accessToken.getAccessToken());
        cache.set(user.getUserId(), user);
        String token = jwtService.generate(new JWTService.Payload<>(user.getUserId()));
        return new Login(token, user);
    }

}
