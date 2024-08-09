package fun.sast.evento.lark.api.v2.client;

import fun.sast.evento.lark.api.v2.value.V2;
import fun.sast.evento.lark.domain.common.value.Pagination;
import fun.sast.evento.lark.domain.event.entity.Event;
import fun.sast.evento.lark.domain.event.service.AttachmentService;
import fun.sast.evento.lark.domain.event.service.EventService;
import fun.sast.evento.lark.domain.event.value.EventQuery;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v2/client/event")
@AllArgsConstructor
class EventController {

    EventService eventService;
    AttachmentService attachmentService;

    @GetMapping("/active")
    List<V2.Event> getActiveEvents() {
        EventQuery query = EventQuery.builder()
                .active(true)
                .build();
        List<Event> events = eventService.query(query);
        return events.stream().map(eventService::mapToV2Event).toList();
    }

    @GetMapping("/latest")
    List<V2.Event> getLatestEvents() {
        EventQuery query = EventQuery.builder()
                .start(LocalDateTime.now())
                .build();
        List<Event> events = eventService.query(query);
        return events.stream().map(eventService::mapToV2Event).toList();
    }

    @GetMapping("/history")
    Pagination<V2.Event> getHistoryEvents(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                          @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        EventQuery query = EventQuery.builder()
                .end(LocalDateTime.now())
                .build();
        Pagination<Event> events = eventService.query(query, page, size);
        return new Pagination<>(
                events.elements().stream().map(eventService::mapToV2Event).toList(),
                events.current(),
                events.total()
        );
    }

    @GetMapping("/{eventId}/attachment")
    List<V2.Attachment> getAttachments(@PathVariable Long eventId) {
        return attachmentService.getAttachments(eventId).stream().map(attachmentService::mapToV2Attachment).toList();
    }
}
