package fun.sast.evento.lark.domain.event.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
@TableName("user")
public class User {
    @TableId(value = "user_id", type = IdType.INPUT)
    private String userId;
    private Integer permission;
    @JsonIgnore // ignore in jwt
    private String refreshToken; // sast-link refresh token
}
