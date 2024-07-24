package fun.sast.evento.lark.infrastructure.response;


import fun.sast.evento.lark.infrastructure.error.BusinessException;
import org.springframework.web.client.HttpStatusCodeException;

public record GlobalResult<T>(boolean success, int errCode, String errMsg, T data) {

    public static <T> GlobalResult<T> success(T data) {
        return new GlobalResult<>(true, 0, "", data);
    }

    public static <T> GlobalResult<T> failure(BusinessException businessException) {
        final var reason = businessException.error();
        return new GlobalResult<>(false, reason.getCode(), reason.getMessage(), null);
    }

    public static <T> GlobalResult<T> failure(HttpStatusCodeException httpStatusCodeException) {
        final int code = httpStatusCodeException.getStatusCode().value();
        return new GlobalResult<>(false, code, httpStatusCodeException.getMessage(), null);
    }

    public final static GlobalResult<Object> empty = new GlobalResult<>(true, 0, "", null);

}