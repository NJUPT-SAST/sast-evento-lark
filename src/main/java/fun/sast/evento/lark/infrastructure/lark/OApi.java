package fun.sast.evento.lark.infrastructure.lark;

import com.lark.oapi.Client;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OApi {

    @Getter
    private final Client client;

    public OApi(@Value("${app.lark.id}") String id, @Value("${app.lark.secret}") String secret) {
        this.client = new Client.Builder(id, secret).build();
    }
}
