package fun.sast.evento.lark.domain.event.service;

import java.util.List;

public interface SubscriptionService {

    String generateCheckInCode(Long eventId);

    Boolean checkIn(Long eventId, String linkId, String code);

    Boolean subscribeEvent(Long eventId, String linkId, Boolean subscribe);

    Boolean subscribeDepartment(String departmentId, String linkId, Boolean subscribe);

    Boolean isSubscribed(Long eventId, String linkId);

    Boolean isCheckedIn(Long eventId, String linkId);

    Boolean isSubscribed(String departmentId, String linkId);

    List<String> getSubscribedUsers(Long eventId);

    List<String> getSubscribedUsers(String departmentId);

    Boolean delete(Long eventId);
}
