package com.gg.recommend.util;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

/**
 * @author guowei
 * @date 2019/1/3
 */
public class DateUtils {
    public static String convertStr2Str(String str) {
        try {
            Date date = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss", Locale.ENGLISH).parse(str);
            if (null != date) {
                return new SimpleDateFormat("yyyyMMdd HH:mm:ss").format(date);
            }
        }
        catch (Exception e) {
        }
        return null;
    }

    public static Date convertStr2Date(String str, String pattern) {
        try {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
            LocalDateTime localDateTime = LocalDateTime.parse(str, dateTimeFormatter);
            return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        }
        catch (Exception e) {
        }
        return null;
    }
}
