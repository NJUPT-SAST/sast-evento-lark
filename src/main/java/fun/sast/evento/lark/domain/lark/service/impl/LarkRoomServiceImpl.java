package fun.sast.evento.lark.domain.lark.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lark.oapi.core.response.RawResponse;
import com.lark.oapi.core.token.AccessTokenType;
import com.lark.oapi.service.vc.v1.model.GetRoomReq;
import com.lark.oapi.service.vc.v1.model.GetRoomResp;
import com.lark.oapi.service.vc.v1.model.ListRoomReq;
import com.lark.oapi.service.vc.v1.model.ListRoomResp;
import fun.sast.evento.lark.domain.lark.service.LarkRoomService;
import fun.sast.evento.lark.domain.lark.value.LarkRoom;
import fun.sast.evento.lark.infrastructure.error.BusinessException;
import fun.sast.evento.lark.infrastructure.error.ErrorEnum;
import fun.sast.evento.lark.infrastructure.lark.OApi;
import fun.sast.evento.lark.infrastructure.utils.TimeUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class LarkRoomServiceImpl implements LarkRoomService {

    @Resource
    private OApi oApi;

    @Override
    public List<LarkRoom> list() {
        try {
            List<LarkRoom> larkRooms = new ArrayList<>();
            String pageToken = null;
            do {
                ListRoomResp resp = oApi.getClient().vc().room().list(ListRoomReq.newBuilder()
                        .pageSize(100) // max page size
                        .pageToken(pageToken)
                        .build());
                if (!resp.success()) {
                    throw new BusinessException(ErrorEnum.LARK_ERROR, resp.getMsg());
                }
                Arrays.stream(resp.getData().getRooms()).forEach(room -> larkRooms.add(new LarkRoom(
                        room.getRoomId(),
                        room.getName(),
                        room.getCapacity()
                )));
                pageToken = resp.getData().getPageToken();
            } while (pageToken != null && !pageToken.isEmpty());
            return larkRooms;
        } catch (Exception e) {
            throw new BusinessException(ErrorEnum.LARK_ERROR, e.getMessage());
        }
    }

    @Override
    public LarkRoom get(String id) {
        try {
            GetRoomResp resp = oApi.getClient().vc().room().get(GetRoomReq.newBuilder()
                    .roomId(id)
                    .build());
            if (!resp.success()) {
                throw new BusinessException(ErrorEnum.LARK_ERROR, resp.getMsg());
            }
            return new LarkRoom(
                    resp.getData().getRoom().getRoomId(),
                    resp.getData().getRoom().getName(),
                    resp.getData().getRoom().getCapacity()
            );
        } catch (Exception e) {
            throw new BusinessException(ErrorEnum.LARK_ERROR, e.getMessage());
        }
    }

    @Override
    public Boolean available(String id, LocalDateTime start, LocalDateTime end) {
        try {
            RawResponse resp = oApi.getClient().post(
                    "https://open.feishu.cn/open-apis/meeting_room/freebusy/batch_get?" +
                            "room_ids=" + id + "&time_min=" + TimeUtils.format(start) + "&time_max=" + TimeUtils.format(end),
                    null,
                    AccessTokenType.App);
            String result = new String(resp.getBody());
            JsonNode node = new ObjectMapper().readTree(result);
            String msg = node.get("msg").asText();
            if ("success".equals(msg)) {
                return node.get("data").get("free_busy").isEmpty();
            } else {
                throw new BusinessException(ErrorEnum.LARK_ERROR, msg);

            }
        } catch (Exception e) {
            throw new BusinessException(ErrorEnum.LARK_ERROR, e.getMessage());
        }
    }
}
