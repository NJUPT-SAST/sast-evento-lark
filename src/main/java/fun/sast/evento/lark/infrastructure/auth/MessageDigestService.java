package fun.sast.evento.lark.infrastructure.auth;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public interface MessageDigestService {
    String encode(String message);
    String encode(String message,String salt);

    char[] chars = ("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz" +
            "1234567890!@#$%^&*()_+").toCharArray();
    default String salt(){
        final Random random = ThreadLocalRandom.current();
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            sb.append(chars[random.nextInt(chars.length)]);
        }
        return sb.toString();
    }

}
