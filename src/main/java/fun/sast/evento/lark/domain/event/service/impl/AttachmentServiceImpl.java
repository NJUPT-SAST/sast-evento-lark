package fun.sast.evento.lark.domain.event.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import fun.sast.evento.lark.api.v2.value.V2;
import fun.sast.evento.lark.domain.event.entity.Attachment;
import fun.sast.evento.lark.domain.event.service.AttachmentService;
import fun.sast.evento.lark.domain.event.service.EventService;
import fun.sast.evento.lark.infrastructure.error.BusinessException;
import fun.sast.evento.lark.infrastructure.error.ErrorEnum;
import fun.sast.evento.lark.infrastructure.oss.OSS;
import fun.sast.evento.lark.infrastructure.repository.AttachmentMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.util.List;

@Service
public class AttachmentServiceImpl implements AttachmentService {

    @Resource
    private AttachmentMapper attachmentMapper;
    @Resource
    private OSS oss;
    @Resource
    private EventService eventService;

    @Override
    public Attachment uploadAttachment(Long eventId, MultipartFile file) {
        if (eventService.get(eventId) == null) {
            throw new BusinessException(ErrorEnum.PARAM_ERROR, "Event not found");
        }
        String key = oss.upload(file);
        Attachment attachment = new Attachment();
        attachment.setEventId(eventId);
        attachment.setKey(key);
        attachmentMapper.insert(attachment);
        return attachment;
    }

    @Override
    public Boolean deleteAttachment(Long eventId, Long attachmentId) {
        Attachment attachment = attachmentMapper.selectById(attachmentId);
        if (attachment == null) {
            throw new BusinessException(ErrorEnum.PARAM_ERROR, "Attachment not found");
        }
        if (!attachment.getEventId().equals(eventId)) {
            throw new BusinessException(ErrorEnum.PARAM_ERROR, "Unexpected attachment.");
        }
        oss.delete(attachment.getKey());
        return attachmentMapper.deleteById(attachmentId) > 0;
    }

    @Override
    public List<Attachment> getAttachments(Long eventId) {
        QueryWrapper<Attachment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("event_id", eventId);
        return attachmentMapper.selectList(queryWrapper);
    }

    @Override
    public V2.Attachment mapToV2(Attachment attachment) {
        return new V2.Attachment(
                attachment.getId(),
                attachment.getEventId().toString(),
                oss.url(attachment.getKey()).toString()
        );
    }
}
