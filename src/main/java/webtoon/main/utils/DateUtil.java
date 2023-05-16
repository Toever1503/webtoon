package webtoon.main.utils;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateUtil {
    public static long countDaysBetween2Date(Date d1, Date d2) {
        long diff = d2.getTime() - d1.getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }
}
