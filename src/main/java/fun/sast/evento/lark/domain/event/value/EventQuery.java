package fun.sast.evento.lark.domain.event.value;

import java.time.LocalDateTime;

public record EventQuery(
        Long id,
        String summary,
        String description,
        LocalDateTime start,
        LocalDateTime end,
        String location,
        String tag,
        String larkMeetingRoomName,
        String larkDepartmentName
) {
}
