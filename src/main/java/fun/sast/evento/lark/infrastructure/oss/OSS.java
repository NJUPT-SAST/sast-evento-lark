package fun.sast.evento.lark.infrastructure.oss;

import org.springframework.web.multipart.MultipartFile;

import java.net.URL;

public interface OSS {
    String upload(MultipartFile file);
    void delete(String key);
    URL url(String key);
}
