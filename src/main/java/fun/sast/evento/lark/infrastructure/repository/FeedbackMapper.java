package fun.sast.evento.lark.infrastructure.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import fun.sast.evento.lark.domain.event.entity.Feedback;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FeedbackMapper extends BaseMapper<Feedback> {
}