package fun.sast.evento.lark;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LarkClientTests {
    @Test
    void client(@Value("${app.lark.id}") String id, @Value("${app.lark.secret}") String secret) {

    }

}
