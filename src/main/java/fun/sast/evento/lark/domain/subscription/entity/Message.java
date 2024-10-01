package fun.sast.evento.lark.domain.subscription.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import fun.sast.evento.lark.domain.common.value.EventState;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("message")
public class Message {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    private Long eventId;
    private EventState state;
    private LocalDateTime time;
}
