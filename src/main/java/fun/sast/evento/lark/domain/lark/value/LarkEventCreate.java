package fun.sast.evento.lark.domain.lark.value;

import com.lark.oapi.service.calendar.v4.model.TimeInfo;

public record LarkEventCreate(
        String summary,
        String description,
        TimeInfo start,
        TimeInfo end,
        String roomId,
        String groupId
) {
}
