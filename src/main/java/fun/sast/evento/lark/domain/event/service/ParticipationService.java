package fun.sast.evento.lark.domain.event.service;

import fun.sast.evento.lark.domain.event.entity.Event;

import java.util.List;

public interface ParticipationService {

    String generateCheckInCode(Long eventId);

    Boolean checkIn(Long eventId, String code);

    Boolean subscribeEvent(Long eventId, Boolean subscribe);

    Boolean subscribeDepartment(String departmentId, Boolean subscribe);

    Boolean isSubscribed(Long eventId);

    Boolean isCheckedIn(Long eventId);

    List<Event> getParticipatedEvents();

    List<Event> getSubscribedEvents();
}