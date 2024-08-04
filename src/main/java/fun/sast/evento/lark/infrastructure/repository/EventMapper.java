package fun.sast.evento.lark.infrastructure.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import fun.sast.evento.lark.domain.event.entity.Event;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EventMapper extends BaseMapper<Event> {
}
