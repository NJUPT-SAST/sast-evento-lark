package fun.sast.evento.lark.domain.lark.value;

import com.lark.oapi.service.calendar.v4.model.TimeInfo;

public record LarkEventUpdate(
        String summary,
        String description,
        TimeInfo start,
        TimeInfo end,
        String roomId
) {
}
