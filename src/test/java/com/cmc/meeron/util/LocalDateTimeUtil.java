package com.cmc.meeron.util;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeUtil {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

    public static String convertTime(LocalTime time) {
        return time.format(formatter);
    }

    public static String toStringTime(LocalTime localTime) {
        return zero(localTime.getHour()) + ":" + zero(localTime.getMinute()) + ":" + zero(localTime.getSecond());
    }

    private static String zero(int number) {
        if (number < 10) {
            return "0" + number;
        }
        return String.valueOf(number);
    }
}
