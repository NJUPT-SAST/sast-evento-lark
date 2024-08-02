package fun.sast.evento.lark.api.v2.client;

import fun.sast.evento.lark.api.v2.value.V2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v2/slide")
public class SlideController {

    @GetMapping("/home")
    public List<V2.Slide> home() {
        return List.of();
    }

    @GetMapping("/event")
    public List<V2.Slide> event(@RequestParam Integer eventId) {
        return List.of();
    }
}
