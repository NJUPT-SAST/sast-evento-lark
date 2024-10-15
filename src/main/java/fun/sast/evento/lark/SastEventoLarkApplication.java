package fun.sast.evento.lark;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SastEventoLarkApplication {

    public static void main(String[] args) {
        SpringApplication.run(SastEventoLarkApplication.class, args);
    }

}
