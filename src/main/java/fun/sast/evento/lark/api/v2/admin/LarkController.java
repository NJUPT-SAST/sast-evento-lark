package fun.sast.evento.lark.api.v2.admin;

import fun.sast.evento.lark.api.v2.value.V2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v2/admin/lark")
class LarkController {

    @GetMapping("/department")
    public List<V2.Department> getAllDepartments() {
        return List.of();
    }

    @GetMapping("/meeting-room")
    public List<V2.MeetingRoom> getAllMeetingRooms() {
        return List.of();
    }
}
