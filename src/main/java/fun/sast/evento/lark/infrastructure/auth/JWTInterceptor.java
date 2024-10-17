package fun.sast.evento.lark.infrastructure.auth;

import com.fasterxml.jackson.core.type.TypeReference;
import fun.sast.evento.lark.api.security.RequirePermission;
import fun.sast.evento.lark.domain.event.entity.User;
import fun.sast.evento.lark.infrastructure.error.BusinessException;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JWTInterceptor implements HandlerInterceptor {

    public static ThreadLocal<User> userHolder = new ThreadLocal<>();
    @Resource
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
        if (handler instanceof HandlerMethod method) {
            if (!method.hasMethodAnnotation(RequirePermission.class)) {
                return true;
            }
            RequirePermission annotation = method.getMethodAnnotation(RequirePermission.class);
            if (annotation == null) {
                return true;
            }
            if (user.getPermission() < annotation.value().getNum()) {
                throw new BusinessException("Permission denied");
            }
        }
        return true;
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                @NonNull Object handler, @Nullable Exception ex) {
        userHolder.remove();
    }
}
