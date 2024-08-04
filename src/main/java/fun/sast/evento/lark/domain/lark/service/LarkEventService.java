package fun.sast.evento.lark.domain.lark.service;

import fun.sast.evento.lark.domain.lark.entity.LarkEvent;
import fun.sast.evento.lark.domain.lark.value.LarkEventCreate;
import fun.sast.evento.lark.domain.lark.value.LarkEventModify;
import fun.sast.evento.lark.domain.lark.value.LarkEventQuery;

import java.util.List;

public interface LarkEventService {
    LarkEvent create(LarkEventCreate create);

    LarkEvent modify(Integer id, LarkEventModify modify);

    Boolean delete(Integer id);

    List<LarkEvent> query(LarkEventQuery query);
}
