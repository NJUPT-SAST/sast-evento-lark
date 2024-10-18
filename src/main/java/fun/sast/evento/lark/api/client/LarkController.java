package fun.sast.evento.lark.api.client;

import fun.sast.evento.lark.api.value.V2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v2/client/lark")
class LarkController {

    @GetMapping("/department")
    List<V2.Department> getAllDepartments() {
        return List.of();
    }
}
