package fun.sast.evento.lark.api.v2.admin;

import fun.sast.evento.lark.api.v2.value.V2;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/admin/event")
class EventController {

    @PostMapping("/create")
    public V2.Event createEvent(@RequestBody V2.CreateEventRequest event) {
        return null;
    }

    @DeleteMapping("/delete/{eventId}")
    public Boolean deleteEvent(@PathVariable Integer eventId) {
        return true;
    }

    @PutMapping("/modify/{eventId}")
    public V2.Event modifyEvent(@PathVariable Integer eventId, @RequestBody V2.UpdateEventRequest event) {
        return null;
    }
}