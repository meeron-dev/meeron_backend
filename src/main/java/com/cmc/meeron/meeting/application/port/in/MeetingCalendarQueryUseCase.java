package com.cmc.meeron.meeting.application.port.in;

import com.cmc.meeron.meeting.application.port.in.response.MonthMeetingsCountResponseDto;
import com.cmc.meeron.meeting.application.port.in.response.DayMeetingResponseDto;
import com.cmc.meeron.meeting.application.port.in.response.YearMeetingsCountResponseDto;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.util.List;

public interface MeetingCalendarQueryUseCase {

    String getType();

    List<Integer> getMeetingDays(Long id, YearMonth yearMonth);

    List<DayMeetingResponseDto> getDayMeetings(Long id, LocalDate localDate);

    List<YearMeetingsCountResponseDto> getMeetingCountPerYear(Long id);

    List<MonthMeetingsCountResponseDto> getMeetingCountPerMonth(Long id, Year year);
}
