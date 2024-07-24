package fun.sast.evento.lark.infrastructure.auth;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static java.nio.charset.StandardCharsets.UTF_8;

@Component
public class MD5 implements MessageDigestService {

    private static final MessageDigest md;

    static {
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @SneakyThrows
    public String encode(String message) {
        return byteArrayToHexString(md.digest(message.getBytes(UTF_8)));
    }

    @Override
    public String encode(String message, String salt) {
        return byteArrayToHexString(md.digest((salt + message).getBytes(UTF_8)));
    }

    private static final String[] HEX_DIGITS = {"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    private String byteArrayToHexString(byte[] b) {
        StringBuilder sb = new StringBuilder();
        for (byte value : b) sb.append(byteToHexString(value));
        return sb.toString();
    }

    private String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n += 256;
        int d1 = n / 16;
        int d2 = n % 16;
        return HEX_DIGITS[d1] + HEX_DIGITS[d2];
    }

}
