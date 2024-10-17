package fun.sast.evento.lark.domain.event.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import fun.feellmoose.model.UserInfo;
import lombok.Data;

@Data
@TableName("user")
public class User {
    private String userId;
    private Integer permission;
    @TableField(exist = false)
    private UserInfo userInfo;
}
