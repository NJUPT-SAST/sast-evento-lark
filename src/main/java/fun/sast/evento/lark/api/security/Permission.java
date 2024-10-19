package fun.sast.evento.lark.api.security;

import lombok.Getter;

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
}
