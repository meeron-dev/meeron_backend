package com.cmc.meeron.meeting.adapter.in;

import com.cmc.meeron.common.exception.meeting.SearchTypeNotFoundException;
import com.cmc.meeron.meeting.application.port.in.MeetingCalendarQueryUseCase;
import com.cmc.meeron.meeting.application.port.in.response.DayMeetingResponseDto;
import com.cmc.meeron.meeting.application.port.in.response.MonthMeetingsCountResponseDto;
import com.cmc.meeron.meeting.application.port.in.response.YearMeetingsCountResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MeetingCalendarQueryUseCaseFactory {

    private final List<MeetingCalendarQueryUseCase> meetingCalendarQueryUseCases;

    private MeetingCalendarQueryUseCase getUseCaseType(String type) {
        return meetingCalendarQueryUseCases.stream()
                .filter(useCase -> useCase.getType().equals(type.toUpperCase()))
                .findFirst()
                .orElseThrow(SearchTypeNotFoundException::new);
    }

    public List<Integer> getMeetingDays(String type, Long id, YearMonth date) {
        return getUseCaseType(type).getMeetingDays(id, date);
    }

    public List<DayMeetingResponseDto> getDayMeetings(String type, Long id, LocalDate date) {
        return getUseCaseType(type).getDayMeetings(id, date);
    }

    public List<YearMeetingsCountResponseDto> getMeetingCountPerYear(String type, Long id) {
        return getUseCaseType(type).getMeetingCountPerYear(id);
    }

    public List<MonthMeetingsCountResponseDto> getMeetingCountPerMonth(String type, Long id, Year year) {
        return getUseCaseType(type).getMeetingCountPerMonth(id, year);
    }
}
