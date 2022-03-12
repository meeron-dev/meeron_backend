package com.cmc.meeron.common.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeUtil {

    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/M/d");
    private static final DateTimeFormatter yearMonthFormatter = DateTimeFormatter.ofPattern("yyyy/M");

    public static String convertTime(LocalTime time) {
        return time.format(timeFormatter);
    }

    public static String nowDate() {
        return LocalDate.now().format(dateFormatter);
    }

    public static String convertDate(LocalDate date) {
        return date.format(dateFormatter);
    }

    public static String convertYearMonth(YearMonth date) {
        return date.format(yearMonthFormatter);
    }
}
