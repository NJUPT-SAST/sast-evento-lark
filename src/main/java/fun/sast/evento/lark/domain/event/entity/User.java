package fun.sast.evento.lark.domain.event.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import fun.feellmoose.model.UserInfo;
import lombok.Data;

@Data
@TableName("user")
public class User {
    @TableId(value = "user_id", type = IdType.INPUT)
    private String userId;
    private Integer permission;
    @TableField(exist = false)
    private UserInfo userInfo;
}
