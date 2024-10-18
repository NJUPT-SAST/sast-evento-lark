package fun.sast.evento.lark.infrastructure.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fun.sast.evento.lark.infrastructure.error.BusinessException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Objects;

@RestControllerAdvice
@SuppressWarnings("NullableProblems")
@Slf4j
public class GlobalResponseAdvice implements ResponseBodyAdvice<Object> {

    @Resource
    private ObjectMapper objectMapper;

    @ExceptionHandler(BusinessException.class)
    public GlobalResult<Object> business(BusinessException e) {
        return GlobalResult.failure(e);
    }

    @ExceptionHandler(RuntimeException.class)
    public GlobalResult<Object> runtime(RuntimeException e) {
        log.error("Unexpected exception", e);
        return GlobalResult.failure(e);
    }

    @ExceptionHandler(HttpStatusCodeException.class)
    public GlobalResult<Object> http(HttpStatusCodeException e) {
        return GlobalResult.failure(e);
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        return switch (body) {
            case null -> GlobalResult.empty;
            case GlobalResult<?> result -> result;
            case Boolean bool ->
                    Objects.requireNonNull(bool) ? GlobalResult.success("ok") : GlobalResult.success("fail");
            case String string -> {
                response.getHeaders().set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
                try {
                    yield objectMapper.writeValueAsString(GlobalResult.success(string));
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
            default -> GlobalResult.success(body);
        };
    }
}
