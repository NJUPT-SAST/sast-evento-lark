package fun.sast.evento.lark.infrastructure.response;


import fun.sast.evento.lark.infrastructure.error.BusinessException;
import org.springframework.web.client.HttpStatusCodeException;

public record GlobalResult<T>(boolean success, int errCode, String errMsg, T data) {

    public static <T> GlobalResult<T> success(T data) {
        return new GlobalResult<>(true, 0, "", data);
    }

    public static <T> GlobalResult<T> failure(BusinessException exception) {
        final var reason = exception.error();
        return new GlobalResult<>(false, reason.getCode(), exception.getMessage(), null);
    }

    public static <T> GlobalResult<T> failure(RuntimeException exception) {
        return new GlobalResult<>(false, 500, exception.getMessage(), null);
    }

    public static <T> GlobalResult<T> failure(HttpStatusCodeException httpStatusCodeException) {
        final int code = httpStatusCodeException.getStatusCode().value();
        return new GlobalResult<>(false, code, httpStatusCodeException.getMessage(), null);
    }

    public final static GlobalResult<Object> empty = new GlobalResult<>(true, 0, "", null);

}
