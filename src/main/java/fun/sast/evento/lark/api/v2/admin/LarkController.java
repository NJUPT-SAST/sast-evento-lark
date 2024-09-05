package fun.sast.evento.lark.api.v2.admin;

import fun.sast.evento.lark.api.v2.value.V2;
import fun.sast.evento.lark.domain.lark.service.LarkDepartmentService;
import fun.sast.evento.lark.domain.lark.service.LarkRoomService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v2/admin/lark")
class LarkController {

    @Resource
    LarkDepartmentService larkDepartmentService;
    @Resource
    LarkRoomService larkRoomService;

    @GetMapping("/department")
    List<V2.Department> getAllDepartments() {
        return larkDepartmentService.list().stream()
                .map(larkDepartmentService::mapToV2)
                .toList();
    }

    @GetMapping("/meeting-room")
    List<V2.Room> getAllMeetingRooms() {
        return larkRoomService.list().stream()
                .map(larkRoomService::mapToV2)
                .toList();
    }

    @GetMapping("/meeting-room/{roomId}/available")
    Boolean isAvailable(@PathVariable String roomId, @RequestParam LocalDateTime start, @RequestParam LocalDateTime end) {
        return larkRoomService.isAvailable(roomId, start, end);
    }
}
