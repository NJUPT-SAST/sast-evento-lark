package fun.sast.evento.lark.api.security;

import fun.sast.evento.lark.infrastructure.error.BusinessException;
import fun.sast.evento.lark.infrastructure.error.ErrorEnum;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Permission {
    LOGIN(0),
    MANAGER(1),
    ADMIN(2),
    INVISIBLE(3);

    private final int num;

    Permission(int num) {
        this.num = num;
    }

    public Permission getPermissionByNum(int num) {
        return Arrays.stream(Permission.values())
                .filter(permission -> permission.num == num)
                .findAny()
                .orElseThrow(() -> new BusinessException(ErrorEnum.DEFAULT, "permission not exist"));
    }
}
