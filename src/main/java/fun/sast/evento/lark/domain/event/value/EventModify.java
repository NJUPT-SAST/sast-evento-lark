package fun.sast.evento.lark.domain.event.value;

public record EventModify(
        String summary,
        String description,
        String start,
        String end,
        String location,
        String tag,
        String larkMeetingRoomId,
        String larkDepartmentId
) {
}
