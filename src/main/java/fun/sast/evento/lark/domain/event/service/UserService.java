package fun.sast.evento.lark.domain.event.service;

import fun.sast.evento.lark.api.value.V2;
import fun.sast.evento.lark.domain.event.entity.User;

public interface UserService {

    V2.Login login(String code, Integer type, String codeVerifier);

    V2.Login refreshToken(String refreshToken);

    V2.User getProfile();

    Boolean assignManagerRole(String userId);
}
