package fun.sast.evento.lark.domain.event.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("event")
public class Event {
    @TableId(value = "id")
    private String id;
    private String summary;
    private String description;
    private LocalDateTime start;
    private LocalDateTime end;
    private Location location;
    private EventDetail detail;
}
//实体类，和其他模块有关联，有影响的部分