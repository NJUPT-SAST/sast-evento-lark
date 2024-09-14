package fun.sast.evento.lark;

import fun.sast.evento.lark.domain.lark.service.LarkDepartmentService;
import fun.sast.evento.lark.domain.lark.service.LarkRoomService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

@SpringBootTest
@Slf4j
class LarkClientTests {
    @Autowired
    LarkDepartmentService larkDepartmentService;
    @Autowired
    LarkRoomService larkRoomService;

    @Test
    void client() {
        log.info(Arrays.toString(larkRoomService.list().toArray()));
        log.info(Arrays.toString(larkDepartmentService.list().toArray()));
    }
}
