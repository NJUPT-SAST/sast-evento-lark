package fun.sast.evento.lark.domain.event.service;

import fun.sast.evento.lark.api.value.V2;
import fun.sast.evento.lark.domain.event.entity.User;

public interface UserService {

    V2.Login login(String code, Integer type, String codeVerifier);

    Boolean assignManagerRole(String userId);

    V2.User mapToV2User(User user);
}
