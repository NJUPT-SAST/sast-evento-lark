package fun.sast.evento.lark.domain.event.service;

import java.util.List;

public interface SubscriptionService {

    String generateCheckInCode(Long eventId);

    Boolean checkIn(Long eventId, String code);

    Boolean subscribeEvent(Long eventId, String linkId, Boolean subscribe);

    Boolean subscribeDepartment(String departmentId, Boolean subscribe);

    Boolean isSubscribed(Long eventId);

    Boolean isCheckedIn(Long eventId);

    Boolean isSubscribed(String departmentId);

    List<String> getSubscribedUsers(Long eventId);

    List<String> getSubscribedUsers(String departmentId);

    Boolean delete(Long eventId);
}
