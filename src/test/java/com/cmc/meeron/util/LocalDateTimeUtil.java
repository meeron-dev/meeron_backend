package com.cmc.meeron.util;

import java.time.LocalTime;

public class LocalDateTimeUtil {

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
