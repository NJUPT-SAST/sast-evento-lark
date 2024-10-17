package fun.sast.evento.lark.infrastructure.auth;

import com.fasterxml.jackson.core.type.TypeReference;
import fun.sast.evento.lark.domain.event.entity.User;
import jakarta.annotation.Resource;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class JWTWebSocketInterceptor implements WebFilter {

    @Resource
    JWTService jwtService;

    @NotNull
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, @NotNull WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String token = request.getHeaders().getFirst("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        User user = jwtService.verify(token.substring(7), new TypeReference<>() {
        });
        // 储存user
        exchange.getAttributes().put("user", user);
        return chain.filter(exchange);
    }
}
