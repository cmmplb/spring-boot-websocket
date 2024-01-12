package com.cmmplb.websocket.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author penglibo
 * @date 2024-01-12 17:55:40
 * @since jdk 1.8
 */
public class DateUtil {

    private static final Object LOCK_OBJ = new Object();

    private static final Map<String, ThreadLocal<DateFormat>> SDF_MAP = new HashMap();

    private static DateFormat getSdf(String pattern) {
        ThreadLocal<DateFormat> tl = (ThreadLocal)SDF_MAP.get(pattern);
        if (tl == null) {
            synchronized(LOCK_OBJ) {
                tl = (ThreadLocal)SDF_MAP.get(pattern);
                if (tl == null) {
                    tl = ThreadLocal.withInitial(() -> {
                        return new SimpleDateFormat(pattern, Locale.CHINA);
                    });
                    SDF_MAP.put(pattern, tl);
                }
            }
        }

        return (DateFormat)tl.get();
    }

    public static String dateFormat(Date dateTime) {
        Date currentDateTime = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateTime);
        int year = calendar.get(1);
        int month = calendar.get(2) + 1;
        int day = calendar.get(5);
        int week = calendar.get(7) - 1;
        int hour = calendar.get(11);
        int minute = calendar.get(12);
        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.setTime(currentDateTime);
        int currentYear = currentCalendar.get(1);
        int currentMonth = currentCalendar.get(2) + 1;
        int currentDay = currentCalendar.get(5);
        if ((currentDateTime.getTime() - dateTime.getTime()) / 1000L <= 60L) {
            return "刚刚";
        } else if (year == currentYear && month == currentMonth && day == currentDay) {
            return String.format("%02d", hour) + ":" + String.format("%02d", minute);
        } else if (year == currentYear && month == currentMonth && day == currentDay - 1) {
            return "昨天 " + String.format("%02d", hour) + ":" + String.format("%02d", minute);
        } else if (year == currentYear && month == currentMonth && currentDay - day < 7) {
            String[] arr = new String[]{"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
            return arr[week] + " " + String.format("%02d", hour) + ":" + String.format("%02d", minute);
        } else {
            return getSdf("yyyy-MM-dd HH:mm").format(dateTime);
        }
    }
}
