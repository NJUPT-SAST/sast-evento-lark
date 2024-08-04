package fun.sast.evento.lark.api.v2.value;

public interface V2 {

    record Attachment(
            Long id,
            String eventId,
            String url
    ) {
    }

    enum EventState {
        SIGNING_UP,
        ACTIVE,
        COMPLETED,
        CANCELLED
    }

    record Event(
            Long id,
            String summary,
            String description,
            String start,
            String end,
            EventState state,
            String location,
            String tag,
            String larkMeetingRoomId,
            String larkMeetingRoomName,
            String larkDepartmentId,
            String larkDepartmentName,
            Boolean isSubscribed,
            Boolean isCheckedIn
    ) {
    }

    record CreateEventRequest(
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

    record UpdateEventRequest(
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

    record Feedback(
            Long id,
            Long linkId,
            Long eventId,
            Integer rating,
            String feedback
    ) {
    }

    record Participation(
            Long id,
            Long linkId,
            Long eventId,
            Boolean isSubscribed,
            Boolean isCheckIn
    ) {
    }

    record Slide(
            Long id,
            String eventId,
            String url,
            String link
    ) {
    }

    record User(
            String linkId,
            String email,
            String avatar,
            String org,
            String nickname
    ) {
    }

    record Department(
            String id,
            String name
    ) {
    }

}
