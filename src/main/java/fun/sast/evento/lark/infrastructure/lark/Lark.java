package fun.sast.evento.lark.infrastructure.lark;

import com.lark.oapi.Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Lark {
    private final Client client;

    public Client client() {
        return this.client;
    }

    public Lark(@Value("${app.lark.id}") String id, @Value("${app.lark.secret}") String secret) {
        this.client = Client.newBuilder(id, secret).build();
    }

}
