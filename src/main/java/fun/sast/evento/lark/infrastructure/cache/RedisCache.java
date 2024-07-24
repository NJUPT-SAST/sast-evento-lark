package fun.sast.evento.lark.infrastructure.cache;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;


@Component
@ConditionalOnProperty(prefix = "app.cache.redis", name = {"prefix", "expire"})
public class RedisCache implements Cache {

    private final RedisTemplate<String, Object> redisTemplate;
    private final String prefix;
    private final Long expire;

    public RedisCache(RedisTemplate<String, Object> redisTemplate,
                      @Value("${app.cache.redis.prefix}") String prefix,
                      @Value("${app.cache.redis.expire}") Long expire) {
        this.redisTemplate = redisTemplate;
        this.prefix = prefix + ":";
        this.expire = expire;
    }

    @Override
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(prefix + key, value, expire);
    }

    @Override
    public Object get(String key) {
        return redisTemplate.opsForValue().get(prefix + key);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> clazz) throws IllegalArgumentException {
        Object value = redisTemplate.opsForValue().get(prefix + key);
        if (value == null) {
            return null;
        }
        if (clazz.isAssignableFrom(value.getClass())) {
            return (T) get(prefix + key);
        }
        throw new IllegalArgumentException("Class " + clazz.getName() + " is not a subclass of " + clazz.getName());
    }

    @Override
    public void remove(String key) {
        redisTemplate.delete(prefix + key);
    }

}
