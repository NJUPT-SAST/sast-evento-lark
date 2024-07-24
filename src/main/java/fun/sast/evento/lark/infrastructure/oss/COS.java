package fun.sast.evento.lark.infrastructure.oss;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.StorageClass;
import com.qcloud.cos.region.Region;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.util.UUID;

@Component
public class COS implements OSS {

    private final COSClient cosClient;
    private final String bucket;

    public COS(@Value("${app.oss.cos.id}") String id,
               @Value("${app.oss.cos.secret}") String secret,
               @Value("${app.oss.cos.region}") String region,
               @Value("${app.oss.cos.bucket}") String bucket) {
        final COSCredentials credentials = new BasicCOSCredentials(id, secret);
        final ClientConfig config = new ClientConfig();
        config.setRegion(new Region(region));
        config.setHttpProtocol(HttpProtocol.https);
        this.cosClient = new COSClient(credentials, config);
        this.bucket = bucket;
    }

    @Override
    @SneakyThrows
    public String upload(MultipartFile file) {
        String key = UUID.randomUUID().toString();
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(file.getSize());
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, key, file.getInputStream(), objectMetadata);
        putObjectRequest.setStorageClass(StorageClass.Standard);
        cosClient.putObject(putObjectRequest);
        return key;
    }

    @Override
    public void delete(String key) {
        cosClient.deleteObject(bucket, key);
    }

    @Override
    public URL url(String key) {
        return cosClient.getObjectUrl(bucket, key);
    }

}
