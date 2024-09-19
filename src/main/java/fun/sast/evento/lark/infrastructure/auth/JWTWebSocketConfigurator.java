package fun.sast.evento.lark.infrastructure.auth;

import com.fasterxml.jackson.core.type.TypeReference;
import fun.sast.evento.lark.domain.link.value.User;
import fun.sast.evento.lark.infrastructure.error.BusinessException;
import fun.sast.evento.lark.infrastructure.error.ErrorEnum;
import jakarta.annotation.Resource;
import jakarta.websocket.HandshakeResponse;
import jakarta.websocket.server.HandshakeRequest;
import jakarta.websocket.server.ServerEndpointConfig;

import java.util.List;

public class JWTWebSocketConfigurator extends ServerEndpointConfig.Configurator {

    @Resource
    JWTService jwtService;

    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        List<String> tokenList = request.getHeaders().get("Authorization");
        if (tokenList == null || tokenList.isEmpty()) {
            throw new BusinessException(ErrorEnum.WEBSOCKET_ERROR, "Unauthorized.");
        }
        String token = tokenList.getFirst().substring(7);
        User user = jwtService.verify(token, new TypeReference<>() {
        });
        sec.getUserProperties().put("user", user);
        super.modifyHandshake(sec, request, response);
    }
}
