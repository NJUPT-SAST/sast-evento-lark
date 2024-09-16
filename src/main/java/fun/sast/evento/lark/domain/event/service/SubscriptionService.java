package fun.sast.evento.lark.domain.event.service;

public interface SubscriptionService {

    String generateCheckInCode(Long eventId);

    Boolean checkIn(Long eventId, String code);

    Boolean subscribeEvent(Long eventId, Boolean subscribe);

    Boolean subscribeDepartment(String departmentId, Boolean subscribe);

    Boolean isSubscribed(Long eventId);

    Boolean isCheckedIn(Long eventId);

    Boolean isSubscribed(String departmentId);

    Boolean delete(Long eventId);
}
