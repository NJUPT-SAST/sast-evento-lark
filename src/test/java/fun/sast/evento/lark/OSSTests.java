package fun.sast.evento.lark;

import fun.sast.evento.lark.infrastructure.oss.OSS;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

@SpringBootTest
@Slf4j
public class OSSTests {
    @Autowired
    OSS oss;

//    @Test
//    public void OSSTest() {
//        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "hello".getBytes());
//        String key = oss.upload(file);
//        log.info("upload success, key:{}", key);
//        String url = oss.url(key).toString();
//        log.info("gen url success, url:{}", url);
//        oss.delete(key);
//        log.info("delete success.");
//    }

//    @Test
//    void remove(){
//        oss.delete("516519b4-7976-476f-9bdb-85b40da0b45");
//    }

}
