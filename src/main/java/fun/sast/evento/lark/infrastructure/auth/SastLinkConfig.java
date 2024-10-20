package fun.sast.evento.lark.infrastructure.auth;

import fun.feellmoose.service.SastLinkService;
import fun.feellmoose.service.impl.HttpClientSastLinkService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class SastLinkConfig {

    @Bean
    SastLinkService sastLinkServiceWeb(@Value("${app.sast-link.link-path}") String path,
                                       @Value("${app.sast-link.web-redirect-uri}") String uri,
                                       @Value("${app.sast-link.web-client-id}") String id,
                                       @Value("${app.sast-link.web-client-secret}") String secret) {
        return new HttpClientSastLinkService.Builder()
                .setClientId(id)
                .setClientSecret(secret)
                .setRedirectUri(uri)
                .setHostName(path)
                .build();
    }

    @Bean
    SastLinkService sastLinkServiceApp(@Value("${app.sast-link.link-path}") String path,
                                       @Value("${app.sast-link.app-redirect-uri}") String uri,
                                       @Value("${app.sast-link.app-client-id}") String id,
                                       @Value("${app.sast-link.app-client-secret}") String secret) {
        return new HttpClientSastLinkService.Builder()
                .setClientId(id)
                .setClientSecret(secret)
                .setRedirectUri(uri)
                .setHostName(path)
                .build();
    }
}
