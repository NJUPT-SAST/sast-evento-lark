package fun.sast.evento.lark.infrastructure.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorEnum {
    DEFAULT(1000),
    PARAM_ERROR(1001),
    LARK_ERROR(1002),
    SSE_ERROR(1003),
    ;
    private final int code;
}
