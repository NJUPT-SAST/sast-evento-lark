package fun.sast.evento.lark.infrastructure.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorEnum {
    DEFAULT(1000, ""),
    PARAM_ERROR(1001, "Parameter error"),
    LARK_ERROR(1002, "Lark error"),
    WEBSOCKET_ERROR(1003, "WebSocket error"),
    ;
    private final int code;
    private final String message;
}
