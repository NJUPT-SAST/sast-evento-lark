package fun.sast.evento.lark.infrastructure.cache;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisCache {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Value("${app.cache.redis.prefix}")
    private String prefix;

    public void set(String key, Object value, long expire, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(prefix + ":" + key, value, expire, timeUnit);
    }

    public Object get(String key) {
        return redisTemplate.opsForValue().get(prefix + ":" + key);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> clazz) throws IllegalArgumentException {
        Object value = redisTemplate.opsForValue().get(prefix + ":" + key);
        if (value == null) {
            return null;
        }
        if (clazz.isAssignableFrom(value.getClass())) {
            return (T) value;
        }
        throw new IllegalArgumentException("Class " + clazz.getName() + " is not a subclass of " + clazz.getName());
    }

    public void remove(String key) {
        redisTemplate.delete(prefix + ":" + key);
    }
}
