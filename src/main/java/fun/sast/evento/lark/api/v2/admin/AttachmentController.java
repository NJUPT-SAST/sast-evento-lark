package fun.sast.evento.lark.api.v2.admin;

import fun.sast.evento.lark.api.v2.value.V2;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/admin/event")
class AttachmentController {

    @PostMapping("/{eventId}/attachment")
    public V2.Attachment uploadAttachment(@PathVariable Long eventId, @RequestParam String url) {
        return null;

    }

    @DeleteMapping("/{eventId}/attachment/{attachmentId}")
    public Boolean deleteAttachment(@PathVariable Long eventId, @PathVariable Long attachmentId) {
        return true;
    }
}
