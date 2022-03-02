package com.cmc.meeron.util;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeUtil {
    
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
    
    public static String convertTime(LocalTime time) {
        return time.format(formatter);
    }
}
