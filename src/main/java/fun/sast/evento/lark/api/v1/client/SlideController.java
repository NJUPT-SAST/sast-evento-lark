package fun.sast.evento.lark.api.v1.client;

import fun.sast.evento.lark.api.v1.value.V1;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/vi/slide")
class SlideController {


    @GetMapping("/event/list")
    public List<V1.Slide> event(@RequestParam Integer eventId) {

        return List.of();
    }

    @GetMapping("/home/list")
    public V1.SlidePage home(@RequestParam(defaultValue = "1", required = false) Integer current,
                                      @RequestParam(defaultValue = "3", required = false) Integer size) {
        return new V1.SlidePage(List.of(), 0);
    }

}
