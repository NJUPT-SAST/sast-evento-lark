package fun.sast.evento.lark.infrastructure.auth;

import com.fasterxml.jackson.core.type.TypeReference;
import fun.sast.evento.lark.domain.link.value.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


@Component
@AllArgsConstructor
@ConditionalOnBean(JWTService.class)
public class JWTInterceptor implements HandlerInterceptor {

    public static ThreadLocal<User> userHolder = new ThreadLocal<>();
    JWTService jwtService;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
        User user = jwtService.verify(token.substring(7), new TypeReference<>() {
        });
        userHolder.set(user);
        return true;
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                @NonNull Object handler, @Nullable Exception ex) {
        userHolder.remove();
    }
}
