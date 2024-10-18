package fun.sast.evento.lark.api.client;

import fun.sast.evento.lark.api.value.V2;
import fun.sast.evento.lark.domain.event.service.AttachmentService;
import jakarta.annotation.Resource;
import org.springframework.cache.annotation.Cacheable;
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
    @Cacheable(cacheNames = "attachments", key = "#eventId")
    public List<V2.Attachment> getAttachments(@PathVariable Long eventId) {
        return attachmentService.getAttachments(eventId).stream().map(attachmentService::mapToV2).toList();
    }
}
