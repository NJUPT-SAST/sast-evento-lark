package fun.sast.evento.lark.domain.event.value;

import java.time.LocalDateTime;

public record EventCreate(
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
