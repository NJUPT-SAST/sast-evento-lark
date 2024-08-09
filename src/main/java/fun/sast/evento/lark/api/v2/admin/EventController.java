package fun.sast.evento.lark.api.v2.admin;

import fun.sast.evento.lark.api.v2.value.V2;
import fun.sast.evento.lark.domain.event.entity.Event;
import fun.sast.evento.lark.domain.event.service.EventService;
import fun.sast.evento.lark.domain.event.value.EventCreate;
import fun.sast.evento.lark.domain.event.value.EventUpdate;
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
        return eventService.mapToV2Event(event);
    }

    @DeleteMapping("/{eventId}/delete")
    public Boolean deleteEvent(@PathVariable Long eventId) {
        return eventService.delete(eventId);
    }

    @PutMapping("/{eventId}/update")
    public V2.Event updateEvent(@PathVariable Long eventId, @RequestBody V2.UpdateEventRequest request) {
        EventUpdate eventUpdate = new EventUpdate(
                request.summary(),
                request.description(),
                request.start(),
                request.end(),
                request.location(),
                request.tag(),
                request.larkMeetingRoomId(),
                request.larkDepartmentId(),
                request.cancelled()
        );
        Event event = eventService.update(eventId, eventUpdate);
        return eventService.mapToV2Event(event);
    }

    @PostMapping("/{eventId}/cancel")
    public V2.Event cancelEvent(@PathVariable Long eventId) {
        Event event = eventService.cancel(eventId);
        return eventService.mapToV2Event(event);
    }
}
