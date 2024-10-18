package fun.sast.evento.lark.api.common;

import fun.sast.evento.lark.api.security.Permission;
import fun.sast.evento.lark.api.security.RequirePermission;
import fun.sast.evento.lark.api.value.V2;
import fun.sast.evento.lark.domain.event.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "commonUserController")
@RequestMapping("/api/v2/user")
class UserController {

    @Resource
    private UserService userService;

    @GetMapping("/profile")
    @RequirePermission(Permission.LOGIN)
    public V2.User getProfile() {
        return userService.getProfile();
    }
}
