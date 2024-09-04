package fun.sast.evento.lark.domain.event.service;

import fun.sast.evento.lark.api.v2.value.V2;
import fun.sast.evento.lark.domain.event.entity.Attachment;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AttachmentService {

    Attachment uploadAttachment(Long eventId, MultipartFile file);

    Boolean deleteAttachment(Long eventId, Long attachmentId);

    List<Attachment> getAttachments(Long eventId);

    V2.Attachment mapToV2Attachment(Attachment attachment);
}
