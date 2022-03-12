package com.cmc.meeron.common.converter;


import org.springframework.core.convert.converter.Converter;

import java.time.YearMonth;

public class StringToYearMonthConverter implements Converter<String, YearMonth> {

    @Override
    public YearMonth convert(String source) {
        String[] yearMonth = source.split("/");
        return YearMonth.of(Integer.parseInt(yearMonth[0]), Integer.parseInt(yearMonth[1]));
    }
}
