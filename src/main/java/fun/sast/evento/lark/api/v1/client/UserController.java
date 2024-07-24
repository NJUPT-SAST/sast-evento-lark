package fun.sast.evento.lark.api.v1.client;

import fun.sast.evento.lark.api.v1.value.V1;
import fun.sast.evento.lark.domain.link.value.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
class UserController {

    @GetMapping("/info")
    public V1.User getUser() {
        return null;
    }

    @PutMapping("/info")
    public Boolean putUser(@RequestBody User user) {
        return true;
    }

    @GetMapping("/subscribe")
    public Boolean subscribe(@RequestParam Integer eventId, @RequestParam Boolean isSubscribe) {
        return true;
    }

    @GetMapping("/subscribed")
    public List<V1.Event> getSubscribed() {
        return List.of();
    }

    @GetMapping("/register")
    public Boolean register(@RequestParam Integer eventId,
                           @RequestParam Boolean isRegister) {
        return true;
    }

    @GetMapping("/registered")
    public List<V1.Event> getRegistered() {
        return List.of();
    }

    @GetMapping("/participate")
    public V1.Participate getParticipation(@RequestParam Integer eventId) {
        return null;
    }

    @GetMapping("/subscribe/department")
    public Boolean subscribeDepartment(@RequestParam Integer departmentId, @RequestParam Boolean isSubscribe) {
        return true;
    }

    @GetMapping("/subscribe/departments")
    public List<V1.Department> getSubscribeDepartment() {
        return List.of();
    }

}
