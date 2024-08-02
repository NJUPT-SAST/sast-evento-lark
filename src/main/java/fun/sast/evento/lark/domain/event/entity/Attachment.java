package fun.sast.evento.lark.domain.event.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("attachment")
public class Attachment {
    private Long id;
    private Long eventId;
    private String url;
}
