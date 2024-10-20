package fun.sast.evento.lark.domain.lark.service.impl;

import com.lark.oapi.service.contact.v3.model.BatchUserReq;
import com.lark.oapi.service.contact.v3.model.BatchUserResp;
import fun.sast.evento.lark.domain.lark.service.LarkUserService;
import fun.sast.evento.lark.domain.lark.value.LarkUser;
import fun.sast.evento.lark.infrastructure.error.BusinessException;
import fun.sast.evento.lark.infrastructure.error.ErrorEnum;
import fun.sast.evento.lark.infrastructure.lark.OApi;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class LarkUserServiceImpl implements LarkUserService {

    @Resource
    private OApi oApi;

    @Override
    public List<LarkUser> list(List<String> users) {
        if (users.isEmpty()) {
            return List.of();
        }
        try {
            String[] userIds = users.toArray(new String[0]);
            BatchUserResp resp = oApi.getClient().contact().user().batch(BatchUserReq.newBuilder().userIds(userIds).build());
            if (!resp.success()) {
                log.error("failed to list room: {}", resp.getMsg());
                throw new BusinessException(ErrorEnum.LARK_ERROR_LIST_USER, resp.getMsg());
            }
            return Arrays.stream(resp.getData().getItems()).map(user -> new LarkUser(
                    user.getOpenId(),
                    user.getName(),
                    user.getAvatar().getAvatar240()
            )).toList();
        } catch (Exception e) {
            log.error("list user error: {}", e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
