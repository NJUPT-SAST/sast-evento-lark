package fun.sast.evento.lark.domain.event.service;

import fun.feellmoose.model.UserInfo;
import fun.sast.evento.lark.api.v2.value.V2;
import fun.sast.evento.lark.domain.event.entity.User;

public interface UserService {

    User getUser(UserInfo userInfo);

    V2.User getProfile();
}
