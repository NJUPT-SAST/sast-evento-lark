package fun.sast.evento.lark.domain.common.value;

import java.util.List;


public record Pagination<T>(
        List<T> elements,
        long current,
        long total
) {
}