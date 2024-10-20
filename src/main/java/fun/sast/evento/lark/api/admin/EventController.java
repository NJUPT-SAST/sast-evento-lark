package fun.sast.evento.lark.api.admin;

import fun.sast.evento.lark.api.security.Permission;
import fun.sast.evento.lark.api.security.RequirePermission;
import fun.sast.evento.lark.api.value.V2;
import fun.sast.evento.lark.domain.event.entity.Event;
import fun.sast.evento.lark.domain.event.service.EventService;
import fun.sast.evento.lark.domain.event.value.EventCreate;
import fun.sast.evento.lark.domain.event.value.EventUpdate;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController(value = "adminEventController")
@RequestMapping("/api/v2/admin/event")
class EventController {

    @Resource
    EventService eventService;

    @PostMapping("/create")
    @RequirePermission(Permission.MANAGER)
    V2.Event createEvent(@RequestBody V2.CreateEventRequest request) {
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
        return eventService.mapToV2(event);
    }

    @DeleteMapping("/{eventId}/delete")
    @RequirePermission(Permission.MANAGER)
    Boolean deleteEvent(@PathVariable Long eventId) {
        return eventService.delete(eventId);
    }

    @PutMapping("/{eventId}/update")
    @RequirePermission(Permission.MANAGER)
    V2.Event updateEvent(@PathVariable Long eventId, @RequestBody V2.UpdateEventRequest request) {
        EventUpdate eventUpdate = new EventUpdate(
                request.summary(),
                request.description(),
                request.start(),
                request.end(),
                request.location(),
                request.tag(),
                request.larkMeetingRoomId(),
                request.cancelled()
        );
        Event event = eventService.update(eventId, eventUpdate);
        return eventService.mapToV2(event);
    }

    @PostMapping("/{eventId}/cancel")
    @RequirePermission(Permission.MANAGER)
    V2.Event cancelEvent(@PathVariable Long eventId) {
        Event event = eventService.cancel(eventId);
        return eventService.mapToV2(event);
    }

    @PostMapping("/{eventId}/invite")
    @RequirePermission(Permission.MANAGER)
    void invite(@PathVariable Long eventId, @RequestBody List<V2.LarkUser> users) {
        eventService.invite(eventId, users);
    }

    @GetMapping("/{eventId}/invite")
    @RequirePermission(Permission.MANAGER)
    List<V2.LarkUser> getAttendees(@PathVariable Long eventId) {
        return eventService.getAttendees(eventId).stream().map(user -> new V2.LarkUser(
                user.openId(),
                user.name(),
                user.avatar()
        )).toList();
    }
}
