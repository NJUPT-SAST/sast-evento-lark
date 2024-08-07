package fun.sast.evento.lark.domain.event.service;

public interface ParticipationService {

    Boolean checkIn(Long eventId, String code);

    Boolean subscribeEvent(Long eventId, Boolean subscribe);

    Boolean subscribeDepartment(String departmentId, Boolean subscribe);

    Boolean isSubscribed(Long eventId, String linkId);

    Boolean isCheckedIn(Long eventId, String linkId);
}
