package fun.sast.evento.lark.infrastructure.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorEnum {
    Default(1000,"")
    ;
    private final int code;
    private final String message;
}
