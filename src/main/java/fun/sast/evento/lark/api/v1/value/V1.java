package fun.sast.evento.lark.api.v1.value;

import java.util.Date;
import java.util.List;

public interface V1 {
    record Event(
            Integer id,
            String title,
            String description,
            Date gmtEventStart,
            Date gmtEventEnd,
            Date gmtRegistrationStart,
            Date gmtRegistrationEnd,
            EventType eventType,
            Integer typeId,
            String location,
            Integer locationId,
            String tag,
            Integer state,//1:未开始,2:报名中,3:进行中,4:已取消,5:已结束
            List<Department> departments
    ) {
    }

    record Department(
            Integer id,
            String departmentName
    ) {
    }

    record EventType(
            Integer id,
            String typeName,
            Boolean allowConflict
    ) {
    }

    record Feedbacks(
            Integer eventId,
            Double average,
            Integer subscribeNum,
            Integer registrationNum,
            Integer participantNum,
            List<Feedback> feedbacks
    ) {
    }

    record Feedback(
            Integer id,
            String content,
            Double score,
            Integer eventId
    ) {
    }

    record FeedbackNum(
            Integer eventId,
            Integer feedbackCount,
            String title
    ) {
    }

    record Page<T>(
            List<T> result,
            Integer current,
            Integer total
    ) {
    }

    record User(
            String id,
            String linkId,
            String studentId,
            String email,
            String nickname,
            String avatar,
            String org,
            String bio,
            List<String> link
    ) {
    }

    record Participate(
            Boolean isRegistration,
            Boolean isParticipate,
            Boolean isSubscribe,
            String userId,
            Integer eventId
    ) {
    }

    record Slide(
            Integer id,
            String title,
            String link,
            String url,
            Integer eventId
    ) {
    }

    //ugly but in google design
    record SlidePage(
            List<Slide> slides,
            int total
    ) {
    }

}


