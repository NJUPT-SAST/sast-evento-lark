package fun.sast.evento.lark.domain.event.value;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record EventQuery(
        Long id,
        String summary,
        String description,
        LocalDateTime start,
        LocalDateTime end,
        String location,
        String tag,
        String larkMeetingRoomName,
        String larkDepartmentName,
        Boolean active,
        Boolean participated,
        Boolean subscribed
) {
}
