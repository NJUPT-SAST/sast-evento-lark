package fun.sast.evento.lark.api.v2.client;

import fun.sast.evento.lark.domain.link.value.User;
import fun.sast.evento.lark.domain.message.service.MessageService;
import fun.sast.evento.lark.infrastructure.auth.JWTWebSocketConfigurator;
import jakarta.annotation.Resource;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/api/v2/client/event/subscribe", configurator = JWTWebSocketConfigurator.class)
public class SubscriptionChannel {

    @Resource
    private MessageService messageService;

    @OnOpen
    public void onOpen(Session session) {
        User user = (User) session.getUserProperties().get("user");
        messageService.connect(user.getUserId(), session);
    }

    @OnClose
    public void onClose(Session session) {
        User user = (User) session.getUserProperties().get("user");
        if (user == null) {
            return;
        }
        messageService.disconnect(user.getUserId());
    }
}
