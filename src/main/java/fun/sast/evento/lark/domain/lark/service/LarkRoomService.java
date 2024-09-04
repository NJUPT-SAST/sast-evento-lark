package fun.sast.evento.lark.domain.lark.service;

import fun.sast.evento.lark.domain.lark.value.LarkRoom;

import java.time.LocalDateTime;
import java.util.List;

public interface LarkRoomService {

    List<LarkRoom> list();

    LarkRoom get(String id);

    Boolean available(String id, LocalDateTime start, LocalDateTime end);
}
