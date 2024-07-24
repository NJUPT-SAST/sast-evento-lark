package fun.sast.evento.lark.infrastructure.cache;

public interface Cache {
    void set(String key, Object value);

    Object get(String key);

    <T> T get(String key, Class<T> clazz);

    void remove(String key);
}
