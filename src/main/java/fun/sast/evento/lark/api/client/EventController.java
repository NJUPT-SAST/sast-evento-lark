package fun.sast.evento.lark.api.client;

import fun.sast.evento.lark.api.value.V2;
import fun.sast.evento.lark.domain.common.value.Pagination;
import fun.sast.evento.lark.domain.event.entity.Event;
import fun.sast.evento.lark.domain.event.service.EventService;
import fun.sast.evento.lark.domain.event.value.EventQuery;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v2/client/event")
@AllArgsConstructor
class EventController {

    EventService eventService;

    @GetMapping("/query")
    Pagination<V2.Event> query(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                               @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
                               @RequestParam(value = "id", required = false) Long id,
                               @RequestParam(value = "summary", required = false) String summary,
                               @RequestParam(value = "description", required = false) String description,
                               @RequestParam(value = "start", required = false) LocalDateTime start,
                               @RequestParam(value = "end", required = false) LocalDateTime end,
                               @RequestParam(value = "location", required = false) String location,
                               @RequestParam(value = "tag", required = false) String tag,
                               @RequestParam(value = "larkMeetingRoomName", required = false) String larkMeetingRoomName,
                               @RequestParam(value = "larkDepartmentName", required = false) String larkDepartmentName,
                               @RequestParam(value = "active", required = false) Boolean active,
                               @RequestParam(value = "participated", required = false) Boolean participated,
                               @RequestParam(value = "subscribed", required = false) Boolean subscribed) {
        EventQuery query = EventQuery.builder()
                .id(id)
                .summary(summary)
                .description(description)
                .start(start)
                .end(end)
                .location(location)
                .tag(tag)
                .larkMeetingRoomName(larkMeetingRoomName)
                .larkDepartmentName(larkDepartmentName)
                .active(active)
                .participated(participated)
                .subscribed(subscribed)
                .build();
        Pagination<Event> events = eventService.query(query, page, size);
        return new Pagination<>(
                events.elements().stream().map(eventService::mapToV2).toList(),
                events.current(),
                events.total()
        );
    }
}
