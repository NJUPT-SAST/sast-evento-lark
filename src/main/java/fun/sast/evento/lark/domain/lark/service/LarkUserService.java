package fun.sast.evento.lark.domain.lark.service;

import fun.sast.evento.lark.domain.lark.value.LarkUser;

import java.util.List;

public interface LarkUserService {

    List<LarkUser> list(List<String> users);
}
