package fun.sast.evento.lark.domain.lark.service;

import fun.sast.evento.lark.domain.lark.value.LarkDepartment;

import java.util.List;

public interface LarkDepartmentService {

    List<LarkDepartment> list();

    LarkDepartment get(String openId);
}
