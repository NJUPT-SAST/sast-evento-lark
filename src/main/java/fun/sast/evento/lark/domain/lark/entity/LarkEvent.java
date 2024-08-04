package fun.sast.evento.lark.domain.lark.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LarkEvent {
    private String id;
    private String summary;
    private String description;
    private Boolean notification;
    private LocalDateTime start;
    private LocalDateTime end;
}
