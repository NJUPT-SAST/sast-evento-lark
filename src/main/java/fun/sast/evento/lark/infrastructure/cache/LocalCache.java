package fun.sast.evento.lark.infrastructure.cache;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * simple cache implement
 */
@Component
@ConditionalOnMissingBean(RedisCache.class)
public class LocalCache implements Cache {

    private static final ConcurrentHashMap<String, Object> cache = new ConcurrentHashMap<>();

    @Override
    public void set(String key, Object value) {
        cache.put(key, value);
    }

    @Override
    public Object get(String key) {
        return cache.get(key);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(String key, Class<T> clazz) {
        Object value = cache.get(key);
        if (value == null) {
            return null;
        }
        if (clazz.isAssignableFrom(value.getClass())) {
            return (T) value;
        }
        return null;
    }

    @Override
    public void remove(String key) {
        cache.remove(key);
    }

}
