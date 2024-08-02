package fun.sast.evento.lark.domain.event.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("feedback")
public class Feedback {
    private Long id;
    private Long linkId;
    private Long eventId;
    private Integer rating;
    private String feedback;
}
