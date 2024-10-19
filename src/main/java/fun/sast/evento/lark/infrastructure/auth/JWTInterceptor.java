package fun.sast.evento.lark.infrastructure.auth;

import com.fasterxml.jackson.core.type.TypeReference;
import fun.sast.evento.lark.api.security.RequirePermission;
import fun.sast.evento.lark.domain.event.entity.User;
import fun.sast.evento.lark.infrastructure.error.BusinessException;
import fun.sast.evento.lark.infrastructure.error.ErrorEnum;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class JWTInterceptor implements HandlerInterceptor {

    public static ThreadLocal<User> userHolder = new ThreadLocal<>();
    @Resource
    private JWTService jwtService;
    @Resource
    private CacheManager cacheManager;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            throw new BusinessException(ErrorEnum.AUTH_ERROR, "Please login first");
        }
        String token = header.substring(7);
        User user = jwtService.verify(token, new TypeReference<>() {
        });
        if (requireLogin(user.getUserId())) {
            throw new BusinessException(ErrorEnum.AUTH_ERROR, "token expired");
        }
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
                throw new BusinessException(ErrorEnum.PERMISSION_DENIED);
            }
        }
        return true;
    }

    private boolean requireLogin(String userId) {
        try {
            Cache cache = cacheManager.getCache("user");
            if (cache == null) {
                log.error("user cache not found");
                return false;
            }
            return cache.get(userId) == null;
        } catch (RuntimeException exception) {
            log.error("failed to check user token cache", exception);
            return false;
        }
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                @NonNull Object handler, @Nullable Exception ex) {
        userHolder.remove();
    }
}
