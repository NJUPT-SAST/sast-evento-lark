package fun.sast.evento.lark.infrastructure.auth;

import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@AllArgsConstructor
class InterceptorConfig implements WebMvcConfigurer {

    JWTInterceptor jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                //TODO delete after test
//                .addPathPatterns("/**")
                .excludePathPatterns("/api/login/**");
    }

}
