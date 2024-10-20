package fun.sast.evento.lark.infrastructure.auth;

import fun.feellmoose.service.SastLinkService;
import fun.feellmoose.service.impl.HttpClientSastLinkService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class SastLinkConfig {

    @Bean
    SastLinkService sastLinkService(@Value("${app.sast-link.link-path}") String path,
                                       @Value("${app.sast-link.client-id}") String id,
                                       @Value("${app.sast-link.client-secret}") String secret) {
        return new HttpClientSastLinkService.Builder()
                .setClientId(id)
                .setClientSecret(secret)
                .setHostName(path)
                .build();
    }
}
