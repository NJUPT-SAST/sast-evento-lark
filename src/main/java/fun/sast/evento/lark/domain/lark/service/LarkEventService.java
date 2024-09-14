package fun.sast.evento.lark.domain.lark.service;

import com.lark.oapi.service.calendar.v4.model.CalendarEvent;
import fun.sast.evento.lark.domain.lark.value.LarkEventCreate;
import fun.sast.evento.lark.domain.lark.value.LarkEventUpdate;

public interface LarkEventService {

    String create(LarkEventCreate create);

    CalendarEvent get(String id);

    void update(String id, LarkEventUpdate update);

    void delete(String id);
}
