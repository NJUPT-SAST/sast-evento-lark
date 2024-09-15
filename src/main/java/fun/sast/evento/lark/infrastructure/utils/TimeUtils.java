package fun.sast.evento.lark.infrastructure.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class TimeUtils {

    private static final DateTimeFormatter RFC3339 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
    private static final ZoneId ZONE = ZoneId.systemDefault();

    public static String toEpochSecond(LocalDateTime localDateTime) {
        return String.valueOf(localDateTime.atZone(ZONE).toEpochSecond());
    }

    public static String format(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZONE).format(RFC3339);
    }

    public static String zone(){
        return ZONE.toString();
    }
}
