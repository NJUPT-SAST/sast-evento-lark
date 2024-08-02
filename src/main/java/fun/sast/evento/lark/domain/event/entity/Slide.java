package fun.sast.evento.lark.domain.event.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("slide")
public class Slide {
    private Long id;
    private String eventId;
    private String url;
    private String link;
}
