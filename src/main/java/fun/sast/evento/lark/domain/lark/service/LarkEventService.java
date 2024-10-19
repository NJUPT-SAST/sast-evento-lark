package fun.sast.evento.lark.domain.lark.service;

import com.lark.oapi.service.calendar.v4.model.CalendarEvent;
import com.lark.oapi.service.calendar.v4.model.CalendarEventAttendee;
import fun.sast.evento.lark.api.value.V2;
import fun.sast.evento.lark.domain.lark.value.LarkEventCreate;
import fun.sast.evento.lark.domain.lark.value.LarkEventUpdate;

import java.util.List;
import java.util.Set;

public interface LarkEventService {

    String create(LarkEventCreate create);

    CalendarEvent get(String id);

    void update(String id, LarkEventUpdate update);

    void delete(String id);

    void invite(String id, List<V2.LarkUser> users);

    Set<CalendarEventAttendee> getAttendees(String id, String type);
}
