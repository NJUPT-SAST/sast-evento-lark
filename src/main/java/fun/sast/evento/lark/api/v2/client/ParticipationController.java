package fun.sast.evento.lark.api.v2.client;

import fun.sast.evento.lark.api.v2.value.V2;
import fun.sast.evento.lark.domain.event.entity.Event;
import fun.sast.evento.lark.domain.event.service.EventService;
import fun.sast.evento.lark.domain.event.service.ParticipationService;
import fun.sast.evento.lark.domain.event.value.EventQuery;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/client/event")
class ParticipationController {

    @Resource
    ParticipationService participationService;
    @Resource
    EventService eventService;

    @PostMapping("/{eventId}/check-in")
    Boolean checkIn(@PathVariable Long eventId, @RequestParam String code) {
        return participationService.checkIn(eventId, code);
    }

    @PostMapping("/{identifier}/subscribe")
    Boolean subscribe(@PathVariable String identifier, @RequestParam Boolean subscribe) {
        if (identifier.matches("\\d+")) {
            Long eventId = Long.parseLong(identifier);
            // 处理活动订阅
            return participationService.subscribeEvent(eventId, subscribe);
        } else {
            // 处理部门订阅
            return participationService.subscribeDepartment(identifier, subscribe);
        }
    }

    @GetMapping("/participated")
    List<V2.Event> getParticipatedEvents() {
        List<Event> events = participationService.getParticipatedEvents();
        return events.stream().map(eventService::mapToV2).toList();
    }

    @GetMapping("/subscribed")
    List<V2.Event> getSubscribedEvents() {
        List<Event> events = participationService.getSubscribedEvents();
        return events.stream().map(eventService::mapToV2).toList();
    }
}
