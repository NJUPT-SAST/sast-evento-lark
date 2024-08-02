package fun.sast.evento.lark.domain.event.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("participation")
public class Participation {
    private Long id;
    private Long linkId;
    private Long eventId;
    private Boolean isSubscribed;
    private Boolean isCheckIn;
}
