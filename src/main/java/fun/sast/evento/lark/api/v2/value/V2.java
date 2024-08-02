package fun.sast.evento.lark.api.v2.value;

public interface V2 {

    record Event(
            Long id,
            String summary,
            String description,
            String start,
            String end,
            String location,
            String tag,
            String attachmentUrl,
            Long like,
            Long dislike,
            String larkEventUid,
            String larkMeetingRoomId,
            String larkMeetingRoomName,
            String larkDepartment
    ) {
    }

    record Feedback(
            Long id,
            Long linkId,
            Long eventId,
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

}
