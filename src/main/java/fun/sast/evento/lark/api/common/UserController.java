package fun.sast.evento.lark.api.common;

import fun.sast.evento.lark.api.v2.value.V2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/user")
class UserController {

    @GetMapping("/profile")
    V2.User getProfile(){
        return null;
    }
}
