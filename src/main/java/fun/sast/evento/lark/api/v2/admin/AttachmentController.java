package fun.sast.evento.lark.api.v2.admin;

import fun.sast.evento.lark.api.v2.value.V2;
import fun.sast.evento.lark.domain.event.entity.Attachment;
import fun.sast.evento.lark.domain.event.service.AttachmentService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController(value = "adminAttachmentController")
@RequestMapping("/api/v2/admin/event")
class AttachmentController {

    @Resource
    AttachmentService attachmentService;

    @PostMapping("/{eventId}/attachment")
    V2.Attachment uploadAttachment(@PathVariable Long eventId, @RequestParam MultipartFile file) {
        Attachment attachment = attachmentService.uploadAttachment(eventId, file);
        return attachmentService.mapToV2(attachment);
    }

    @DeleteMapping("/{eventId}/attachment/{attachmentId}")
    Boolean deleteAttachment(@PathVariable Long eventId, @PathVariable Long attachmentId) {
        return attachmentService.deleteAttachment(eventId, attachmentId);
    }
}
