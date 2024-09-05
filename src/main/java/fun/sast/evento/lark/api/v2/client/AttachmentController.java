package fun.sast.evento.lark.api.v2.client;

import fun.sast.evento.lark.api.v2.value.V2;
import fun.sast.evento.lark.domain.event.service.AttachmentService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v2/client/event")
class AttachmentController {

    @Resource
    AttachmentService attachmentService;

    @GetMapping("/{eventId}/attachment")
    List<V2.Attachment> getAttachments(@PathVariable Long eventId) {
        return attachmentService.getAttachments(eventId).stream().map(attachmentService::mapToV2).toList();
    }
}
