package fun.sast.evento.lark.api.admin;

import fun.sast.evento.lark.api.security.Permission;
import fun.sast.evento.lark.api.security.RequirePermission;
import fun.sast.evento.lark.domain.event.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/admin/permission")
public class PermissionController {

    @Resource
    private UserService userService;

    @PostMapping("/manager")
    @RequirePermission(Permission.ADMIN)
    public Boolean assignManagerRole(String userId) {
        return userService.assignManagerRole(userId);
    }
}
