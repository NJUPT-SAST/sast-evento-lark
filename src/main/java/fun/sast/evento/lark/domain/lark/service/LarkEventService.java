package fun.sast.evento.lark.domain.lark.service;

import fun.sast.evento.lark.domain.lark.value.LarkEventCreate;
import fun.sast.evento.lark.domain.lark.value.LarkEventModify;

public interface LarkEventService {

    String create(LarkEventCreate create);

    void modify(String id, LarkEventModify modify);

    void delete(String id);
}
