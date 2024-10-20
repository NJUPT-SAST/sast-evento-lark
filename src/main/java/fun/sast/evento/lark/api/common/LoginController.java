package fun.sast.evento.lark.api.common;

import fun.sast.evento.lark.api.value.V2;
import fun.sast.evento.lark.domain.event.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/login")
class LoginController {

    @Resource
    UserService userService;

    @PostMapping("/link")
    V2.Login link(@RequestParam String code, @RequestParam Integer type, @RequestParam String codeVerifier) {
        return userService.login(code, type, codeVerifier);
    }
}
