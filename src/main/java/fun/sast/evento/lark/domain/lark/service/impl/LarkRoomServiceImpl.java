package fun.sast.evento.lark.domain.lark.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lark.oapi.core.response.RawResponse;
import com.lark.oapi.core.token.AccessTokenType;
import com.lark.oapi.service.vc.v1.model.GetRoomReq;
import com.lark.oapi.service.vc.v1.model.GetRoomResp;
import com.lark.oapi.service.vc.v1.model.ListRoomReq;
import com.lark.oapi.service.vc.v1.model.ListRoomResp;
import fun.sast.evento.lark.api.value.V2;
import fun.sast.evento.lark.domain.lark.service.LarkRoomService;
import fun.sast.evento.lark.domain.lark.value.LarkRoom;
import fun.sast.evento.lark.infrastructure.error.BusinessException;
import fun.sast.evento.lark.infrastructure.error.ErrorEnum;
import fun.sast.evento.lark.infrastructure.lark.OApi;
import fun.sast.evento.lark.infrastructure.utils.TimeUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class LarkRoomServiceImpl implements LarkRoomService {

    @Resource
    private OApi oApi;

    @Override
    public List<LarkRoom> list() {
        try {
            List<LarkRoom> larkRooms = new ArrayList<>();
            String pageToken = null;
            boolean hasMore;
            do {
                ListRoomResp resp = oApi.getClient().vc().room().list(ListRoomReq.newBuilder()
                        .pageSize(100) // max page size
                        .pageToken(pageToken)
                        .build());
                if (!resp.success()) {
                    log.error("failed to list room: {}", resp.getMsg());
                    throw new BusinessException(ErrorEnum.LARK_ERROR_LIST_ROOM, resp.getMsg());
                }
                if (resp.getData().getRooms() != null) {
                    Arrays.stream(resp.getData().getRooms()).forEach(room -> larkRooms.add(new LarkRoom(
                            room.getRoomId(),
                            room.getName(),
                            room.getCapacity()
                    )));
                }
                pageToken = resp.getData().getPageToken();
                hasMore = resp.getData().getHasMore();
            } while (hasMore);
            return larkRooms;
        } catch (Exception e) {
            log.error("list room error: {}", e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public LarkRoom get(String id) {
        try {
            GetRoomResp resp = oApi.getClient().vc().room().get(GetRoomReq.newBuilder()
                    .roomId(id)
                    .build());
            if (!resp.success()) {
                log.error("failed to get room: {}", resp.getMsg());
                throw new BusinessException(ErrorEnum.LARK_ERROR_GET_ROOM, resp.getMsg());
            }
            return new LarkRoom(
                    resp.getData().getRoom().getRoomId(),
                    resp.getData().getRoom().getName(),
                    resp.getData().getRoom().getCapacity()
            );
        } catch (Exception e) {
            log.error("get room error: {}", e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public Boolean isAvailable(String id, LocalDateTime start, LocalDateTime end) {
        try {
            String url = "https://open.feishu.cn/open-apis/meeting_room/freebusy/batch_get?" +
                    "room_ids=" + id +
                    "&time_min=" + URLEncoder.encode(TimeUtils.format(start), StandardCharsets.UTF_8) +
                    "&time_max=" + URLEncoder.encode(TimeUtils.format(end), StandardCharsets.UTF_8);
            RawResponse resp = oApi.getClient().post(
                    url,
                    null,
                    AccessTokenType.App);
            String result = new String(resp.getBody());
            JsonNode node = new ObjectMapper().readTree(result);
            String msg = node.get("msg").asText();
            if ("success".equals(msg)) {
                return node.get("data").get("free_busy").isEmpty();
            } else {
                log.error("failed to check room availability: {}", msg);
                throw new BusinessException(ErrorEnum.LARK_ERROR_CHECK_ROOM_AVAILABLE, msg);

            }
        } catch (Exception e) {
            log.error("check room availability error: {}", e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public V2.Room mapToV2(LarkRoom larkRoom) {
        return new V2.Room(
                larkRoom.id(),
                larkRoom.name(),
                larkRoom.capacity()
        );
    }
}
