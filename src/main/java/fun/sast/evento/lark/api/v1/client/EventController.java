package fun.sast.evento.lark.api.v1.client;

import fun.sast.evento.lark.api.v1.value.V1;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController(value = "v1EventController")
@RequestMapping("/api/v1/event")
class EventController {

    @GetMapping("/conducting")
    public List<V1.Event> getConducting() {
        return List.of();
    }

    @GetMapping("/newest")
    public List<V1.Event> getNewest() {
        return List.of();
    }

    @GetMapping("/history")
    public List<V1.Event> getHistory() {
        return List.of();
    }

    @DeleteMapping("/info")
    public Boolean deleteEvent(@RequestParam Integer eventId) {
        return true;
    }

    @GetMapping("/info")
    public V1.Event getEvent(@RequestParam Integer eventId) {
        return null;
    }

    @GetMapping("/list")
    public V1.Page<V1.Event> getEvents(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                       @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        return new V1.Page<>(List.of(), 1, 10);
    }

    @PostMapping("/list")
    public List<V1.Event> postForEvents(@RequestParam(required = false) List<Integer> typeId,
                                        @RequestParam(required = false) List<Integer> departmentId,
                                        @RequestParam(required = false) String time) {
        return List.of();
    }

    @GetMapping("/departments")
    public List<V1.Department> getDepartmentsWithFilter() {
        return List.of();
    }

}

