package fun.sast.evento.lark;

import com.lark.oapi.service.calendar.v4.enums.CalendarCalendarAccessRoleEnum;
import com.lark.oapi.service.calendar.v4.model.Calendar;
import com.lark.oapi.service.calendar.v4.model.ListCalendarReq;
import com.lark.oapi.service.calendar.v4.model.ListCalendarResp;
import fun.sast.evento.lark.infrastructure.lark.OApi;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

@SpringBootTest
public class LarkTest {

    @Resource
    OApi oApi;

    @Test
    void calendar() throws Exception {
//        Calendar calendar = Calendar.newBuilder()
//                .summary("科协公开活动")
//                .description("由evento管理")
//                .permissions("public")
//                .role(CalendarCalendarAccessRoleEnum.OWNER)
//                .build();
//        CreateCalendarResp resp = oApi.getClient().calendar().calendar().create(CreateCalendarReq.newBuilder().calendar(calendar).build());
//
//        System.out.println(resp.getMsg());
//        System.out.println(resp.getData().getCalendar().getCalendarId());

        ListCalendarResp resp = oApi.getClient().calendar().calendar().list(ListCalendarReq.newBuilder().build());
        Arrays.stream(resp.getData().getCalendarList()).forEach(calendar -> {
            System.out.println(calendar.getSummary());
            System.out.println(calendar.getCalendarId());
        });
    }
}
