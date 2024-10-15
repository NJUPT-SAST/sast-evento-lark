package fun.sast.evento.lark.infrastructure.lark;

import com.lark.oapi.event.EventDispatcher;
import com.lark.oapi.service.contact.ContactService;
import com.lark.oapi.service.contact.v3.model.P1DepartmentChangedV3;
import com.lark.oapi.service.vc.VcService;
import com.lark.oapi.service.vc.v1.model.P2RoomCreatedV1;
import com.lark.oapi.service.vc.v1.model.P2RoomDeletedV1;
import com.lark.oapi.service.vc.v1.model.P2RoomUpdatedV1;
import com.lark.oapi.ws.Client;
import jakarta.annotation.Resource;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

@Component
public class WSApi {

    @Getter
    private final Client client;
    @Resource
    private CacheManager cacheManager;

    public WSApi(@Value("${app.lark.id}") String id, @Value("${app.lark.secret}") String secret) {
        this.client = new Client.Builder(id, secret).eventHandler(
                EventDispatcher.newBuilder("", "")
                        .onP1DepartmentChangedV3(new ContactService.P1DepartmentChangedV3Handler() {
                            @Override
                            public void handle(P1DepartmentChangedV3 event) {
                                removeCacheIfExists("lark-department", "");
                            }
                        })
                        .onP2RoomUpdatedV1(new VcService.P2RoomUpdatedV1Handler() {
                            @Override
                            public void handle(P2RoomUpdatedV1 event) {
                                removeCacheIfExists("lark-room", "");
                            }
                        })
                        .onP2RoomCreatedV1(new VcService.P2RoomCreatedV1Handler() {
                            @Override
                            public void handle(P2RoomCreatedV1 event) {
                                removeCacheIfExists("lark-room", "");
                            }
                        })
                        .onP2RoomDeletedV1(new VcService.P2RoomDeletedV1Handler() {
                            @Override
                            public void handle(P2RoomDeletedV1 event) {
                                removeCacheIfExists("lark-room", "");
                            }
                        })
                        .build()
        ).build();
    }

    private void removeCacheIfExists(String name, String key) {
        Cache cache = cacheManager.getCache(name);
        if (cache == null) {
            return;
        }
        cache.evictIfPresent(key);
    }
}
