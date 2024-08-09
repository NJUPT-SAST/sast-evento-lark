package fun.sast.evento.lark.api.v2.value;

import fun.sast.evento.lark.domain.common.value.EventState;

import java.time.LocalDateTime;

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
            String larkDepartmentId,
            Boolean cancelled
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

    record MeetingRoom(
            String id,
            String name,
            Integer capacity,
            Boolean status,
            Boolean scheduleStatus,
            String disableStartTime,
            String disableEndTime
    ) {
    }

}
