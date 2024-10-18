package fun.sast.evento.lark.api.admin;

import fun.sast.evento.lark.api.security.Permission;
import fun.sast.evento.lark.api.security.RequirePermission;
import fun.sast.evento.lark.api.value.V2;
import fun.sast.evento.lark.domain.lark.service.LarkDepartmentService;
import fun.sast.evento.lark.domain.lark.service.LarkRoomService;
import jakarta.annotation.Resource;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController(value = "adminLarkController")
@RequestMapping("/api/v2/admin/lark")
class LarkController {

    @Resource
    LarkDepartmentService larkDepartmentService;
    @Resource
    LarkRoomService larkRoomService;

    @GetMapping("/department")
    @Cacheable("lark-department")
    @RequirePermission(Permission.MANAGER)
    public List<V2.Department> getAllDepartments() {
        return larkDepartmentService.list().stream()
                .map(larkDepartmentService::mapToV2)
                .toList();
    }

    @GetMapping("/meeting-room")
    @Cacheable("lark-room")
    @RequirePermission(Permission.MANAGER)
    public List<V2.Room> getAllMeetingRooms() {
        return larkRoomService.list().stream()
                .map(larkRoomService::mapToV2)
                .toList();
    }

    @GetMapping("/meeting-room/{roomId}/available")
    @RequirePermission(Permission.MANAGER)
    public Boolean isAvailable(@PathVariable String roomId, @RequestParam LocalDateTime start, @RequestParam LocalDateTime end) {
        return larkRoomService.isAvailable(roomId, start, end);
    }
}
