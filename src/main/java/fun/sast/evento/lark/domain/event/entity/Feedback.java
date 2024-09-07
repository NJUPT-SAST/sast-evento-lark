package fun.sast.evento.lark.domain.event.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("feedback")
public class Feedback {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    private Long eventId;
    private String linkId;
    private Integer rating;
    private String content;
}
