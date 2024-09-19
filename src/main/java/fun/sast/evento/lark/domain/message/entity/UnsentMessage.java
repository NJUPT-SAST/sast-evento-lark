package fun.sast.evento.lark.domain.message.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("unsent_message")
public class UnsentMessage {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    private Long messageId;
    private String linkId;
}
