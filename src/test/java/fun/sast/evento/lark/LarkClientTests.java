package fun.sast.evento.lark;

import fun.sast.evento.lark.domain.lark.service.LarkDepartmentService;
import fun.sast.evento.lark.domain.lark.service.LarkEventService;
import fun.sast.evento.lark.domain.lark.service.LarkRoomService;
import fun.sast.evento.lark.infrastructure.lark.OApi;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LarkClientTests {
    @Autowired
    LarkDepartmentService larkDepartmentService;
    @Autowired
    LarkRoomService larkRoomService;
    @Autowired
    LarkEventService larkEventService;
    @Autowired
    OApi oApi;

    @Test
    void client() {
        larkDepartmentService.list().forEach(larkDepartment -> {
            System.out.println(larkDepartment.name());
            System.out.println(larkDepartment.openId());
        });
        larkRoomService.list().forEach(larkRoom -> {
            System.out.println(larkRoom.name());
            System.out.println(larkRoom.id());
        });
        /*System.out.println(larkEventService.create(new LarkEventCreate(
                "test summary 2",
                "test description",
                new TimeInfo.Builder()
                        .timestamp(TimeUtils.toEpochSecond(LocalDateTime.now()))
                        .timezone(TimeUtils.zone())
                        .build(),
                new TimeInfo.Builder()
                        .timestamp(TimeUtils.toEpochSecond(LocalDateTime.now().plusHours(1)))
                        .timezone(TimeUtils.zone())
                        .build(),
                "omm_203fe8e78aa4a73f9de528b87d6c32d1", "oc_0f9aa47d5a00fbc91870726fd8f56d2b"
        )));*/

/*        larkEventService.update("44415b8e-53b9-4604-bd4c-1dc0af7775e1_0", new LarkEventUpdate(
                "test summary 4",
                null,
                new TimeInfo.Builder()
                        .timestamp(TimeUtils.toEpochSecond(LocalDateTime.now()))
                        .timezone(TimeUtils.zone())
                        .build(),
                new TimeInfo.Builder()
                        .timestamp(TimeUtils.toEpochSecond(LocalDateTime.now().plusHours(1)))
                        .timezone(TimeUtils.zone())
                        .build(),
                null, "od-64e02d30d83fe3d587e91d25da1fab95"
        ));*/

        /*larkEventService.delete("881d784e-eae5-41c5-9640-4bef0321fe73_0");*/
    }
}
