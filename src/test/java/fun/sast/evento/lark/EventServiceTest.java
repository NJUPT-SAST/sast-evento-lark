package fun.sast.evento.lark;

import fun.sast.evento.lark.domain.event.entity.User;
import fun.sast.evento.lark.domain.event.service.EventService;
import fun.sast.evento.lark.domain.event.service.SubscriptionService;
import fun.sast.evento.lark.domain.event.value.EventCreate;
import fun.sast.evento.lark.domain.event.value.EventQuery;
import fun.sast.evento.lark.domain.lark.service.LarkRoomService;
import fun.sast.evento.lark.domain.lark.service.impl.LarkDepartmentServiceImpl;
import fun.sast.evento.lark.infrastructure.auth.JWTInterceptor;
import fun.sast.evento.lark.infrastructure.auth.JWTService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
public class EventServiceTest {

    @Autowired
    EventService eventService;
    @Autowired
    LarkDepartmentServiceImpl larkDepartmentService;
    @Autowired
    LarkRoomService larkRoomService;
    @Autowired
    JWTService jwtService;
    @Autowired
    SubscriptionService subscriptionService;

    @Test
    void createTestEvent(){
        eventService.create(new EventCreate(
                "this is a test event",
                "test description",
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(1).plusHours(2),
                "test location",
                "tag",
                "omm_203fe8e78aa4a73f9de528b87d6c32d1",
                "od-64e02d30d83fe3d587e91d25da1fab95"
        ));
    }

    @Test
    void deleteTestEvent(){
        eventService.delete(1846897410146017281L);
    }

    @Test
    void queryEvent(){
        eventService.query(new EventQuery(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                true,
                null
        ), 1, 1).elements().forEach(System.out::println);
    }

    @Test
    void print(){
        larkDepartmentService.list().forEach(System.out::println);
        larkRoomService.list().forEach(System.out::println);
    }

    @Test
    void subscribe() {
        User user = new User();
        user.setUserId("123");
        user.setPermission(0);
        JWTInterceptor.userHolder.set(user);
        System.out.println(jwtService.generate(new JWTService.Payload<>(user)));

        subscriptionService.subscribeEvent(1847132458277367809L, "123", true);
    }

    @Test
    void admin() {
        User user = new User();
        user.setUserId("B23051508");
        user.setPermission(2);
        JWTInterceptor.userHolder.set(user);
        System.out.println(jwtService.generate(new JWTService.Payload<>(user)));
    }
}
