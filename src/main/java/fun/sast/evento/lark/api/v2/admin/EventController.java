package fun.sast.evento.lark.api.v2.admin;

import fun.sast.evento.lark.api.v2.value.V2;
import fun.sast.evento.lark.domain.event.entity.Event;
import fun.sast.evento.lark.domain.event.service.EventService;
import fun.sast.evento.lark.domain.event.value.EventCreate;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/admin/event")
class EventController {

    @Resource
    private EventService eventService;

    @PostMapping("/create")
    public V2.Event createEvent(@RequestBody V2.CreateEventRequest request) {
        EventCreate eventCreate = new EventCreate(
                request.summary(),
                request.description(),
                request.start(),
                request.end(),
                request.location(),
                request.tag(),
                request.larkMeetingRoomId(),
                request.larkDepartmentId()
        );
        Event event = eventService.create(eventCreate);
        return new V2.Event(
                event.getId(),
                event.getSummary(),
                event.getDescription(),
                event.getStart(),
                event.getEnd(),
                eventService.calculateState(event),
                event.getLocation(),
                event.getTag(),
                event.getLarkMeetingRoomName(),
                event.getLarkDepartmentName(),
                false,
                false
        );
    }

    @DeleteMapping("/{eventId}/delete")
    public Boolean deleteEvent(@PathVariable Integer eventId) {
        return true;
    }

    @PutMapping("/{eventId}/update")
    public V2.Event updateEvent(@PathVariable Integer eventId, @RequestBody V2.UpdateEventRequest event) {
        return null;
    }

    @PostMapping("/{eventId}/cancel")
    public V2.Event cancelEvent(@PathVariable Integer eventId) {
        return null;
    }
}
