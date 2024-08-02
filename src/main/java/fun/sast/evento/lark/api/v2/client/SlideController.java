package fun.sast.evento.lark.api.v2.client;

import fun.sast.evento.lark.api.v2.value.V2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v2/client/slide")
class SlideController {

    @GetMapping("/home")
    public List<V2.Slide> home() {
        return List.of();
    }

    @GetMapping("/{eventId}")
    public List<V2.Slide> event(@PathVariable Integer eventId) {
        return List.of();
    }
}
