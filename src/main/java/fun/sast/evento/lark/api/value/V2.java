package fun.sast.evento.lark.api.value;

import fun.feellmoose.model.Badge;
import fun.sast.evento.lark.domain.common.value.EventState;

import java.time.LocalDateTime;
import java.util.List;

public interface V2 {

    record Attachment(
            Long id,
            String eventId,
            String url
    ) {
    }

    record Event(
            Long id,
            String summary,
            String description,
            LocalDateTime start,
            LocalDateTime end,
            EventState state,
            String location,
            String tag,
            String larkMeetingRoomName,
            String larkDepartmentName,
            Boolean isSubscribed,
            Boolean isCheckedIn
    ) {
    }

    record CreateEventRequest(
            String summary,
            String description,
            LocalDateTime start,
            LocalDateTime end,
            String location,
            String tag,
            String larkMeetingRoomId,
            String larkDepartmentId
    ) {
    }

    record UpdateEventRequest(
            String summary,
            String description,
            LocalDateTime start,
            LocalDateTime end,
            String location,
            String tag,
            String larkMeetingRoomId,
            Boolean cancelled
    ) {
    }

    record Feedback(
            Long id,
            Long eventId,
            String linkId,
            Integer rating,
            String content
    ) {
    }

    record Slide(
            Long id,
            String eventId,
            String url,
            String link
    ) {
    }

    record Login(
            String token,
            User user
    ){
    }

    record User(
            String linkId,
            String email,
            String avatar,
            Badge badge,
            String bio,
            String dep,
            List<String> hide,
            List<String> link,
            String nickname,
            String org
    ) {
    }

    record Department(
            String id,
            String name
    ) {
    }

    record Room(
            String id,
            String name,
            Integer capacity
    ) {
    }

}
