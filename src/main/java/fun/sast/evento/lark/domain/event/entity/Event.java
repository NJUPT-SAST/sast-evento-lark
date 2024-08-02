package fun.sast.evento.lark.domain.event.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 *
 */
@Data
@TableName("event")
public class Event {
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private Long id;
    private String summary;
    private String description;

    private LocalDateTime start;
    private LocalDateTime end;
    private String location;
    private String tag;
    private String attachmentUrl;
    private String larkEventUid;
    private String larkMeetingRoomId;
    private String larkMeetingRoomName;
    private String larkDepartment;
}
//实体类，和其他模块有关联，有影响的部分