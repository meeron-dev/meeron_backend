package com.cmc.meeron.meeting.application.service;

import com.cmc.meeron.meeting.application.port.in.response.MonthMeetingsCountResponseDto;
import com.cmc.meeron.meeting.application.port.out.response.MonthMeetingsCountQueryDto;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

class MonthMeetingsPerMonthMapper {

    static List<MonthMeetingsCountResponseDto> toMonthMeetingsCountResponseDtoSortByMonth(List<MonthMeetingsCountQueryDto> monthMeetingsCountQueryDtos) {
        List<MonthMeetingsCountResponseDto> monthMeetingsCountResponseDtos = monthMeetingsCountQueryDtos.stream()
                .map(count -> MonthMeetingsCountResponseDto.builder()
                        .month(count.getMonth())
                        .count(count.getCount())
                        .build())
                .collect(Collectors.toList());
        for (int month = 1; month <= 12; month++) {
            int finalMonth = month;
            if (monthMeetingsCountResponseDtos.stream().filter(dto -> dto.getMonth() == finalMonth).findFirst().isEmpty()) {
                monthMeetingsCountResponseDtos.add(MonthMeetingsCountResponseDto.builder()
                        .month(finalMonth)
                        .count(0L)
                        .build());
            }
        }
        Collections.sort(monthMeetingsCountResponseDtos);
        return monthMeetingsCountResponseDtos;
    }
}
