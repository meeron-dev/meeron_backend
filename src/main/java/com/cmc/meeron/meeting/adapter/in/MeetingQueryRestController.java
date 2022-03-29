package com.cmc.meeron.meeting.adapter.in;

import com.cmc.meeron.meeting.adapter.in.request.*;
import com.cmc.meeron.meeting.adapter.in.response.*;
import com.cmc.meeron.meeting.application.port.in.MeetingQueryUseCase;
import com.cmc.meeron.meeting.application.port.in.response.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/meetings")
public class MeetingQueryRestController {

    private final MeetingQueryUseCase meetingQueryUseCase;
    private final MeetingCalendarQueryUseCaseFactory meetingCalendarQueryUseCaseFactory;

    @GetMapping("/today")
    @ResponseStatus(HttpStatus.OK)
    public TodayMeetingResponse todayMeetings(@Valid TodayMeetingRequest todayMeetingRequest) {
        List<TodayMeetingResponseDto> todayMeetings = meetingQueryUseCase
                .getTodayMeetings(todayMeetingRequest.toRequestDto());
        return TodayMeetingResponse.fromDto(todayMeetings);
    }

    @GetMapping("/days")
    @ResponseStatus(HttpStatus.OK)
    public MeetingDaysResponse getMeetingDays(@Valid MeetingDaysRequest meetingDaysRequest) {
        List<Integer> days = meetingCalendarQueryUseCaseFactory.getMeetingDays(meetingDaysRequest.getType(),
                meetingDaysRequest.getId(),
                meetingDaysRequest.getDate());
        return MeetingDaysResponse.of(days);
    }

    @GetMapping("/day")
    @ResponseStatus(HttpStatus.OK)
    public DayMeetingsResponse getDayMeetings(@Valid DayMeetingsRequest dayMeetingsRequest) {
        List<DayMeetingResponseDto> dayMeetingResponseDtos = meetingCalendarQueryUseCaseFactory.getDayMeetings(dayMeetingsRequest.getType(),
                dayMeetingsRequest.getId(),
                dayMeetingsRequest.getDate());
        return DayMeetingsResponse.fromResponseDtos(dayMeetingResponseDtos);
    }

    @GetMapping("/years")
    @ResponseStatus(HttpStatus.OK)
    public YearMeetingsCountResponse getYearMeetingsCount(@Valid YearMeetingsCountRequest yearMeetingsCountRequest) {
        List<YearMeetingsCountResponseDto> meetingCountPerYear = meetingCalendarQueryUseCaseFactory.getMeetingCountPerYear(
                yearMeetingsCountRequest.getType(),
                yearMeetingsCountRequest.getId());
        return YearMeetingsCountResponse.of(meetingCountPerYear);
    }

    @GetMapping("/months")
    @ResponseStatus(HttpStatus.OK)
    public MonthMeetingsCountResponse getMonthMeetingsCount(@Valid MonthMeetingsCountRequest monthMeetingsCountRequest) {
        List<MonthMeetingsCountResponseDto> meetingCountPerMonth = meetingCalendarQueryUseCaseFactory.getMeetingCountPerMonth(
                monthMeetingsCountRequest.getType(),
                monthMeetingsCountRequest.getId(),
                monthMeetingsCountRequest.getYear());
        return MonthMeetingsCountResponse.of(meetingCountPerMonth);
    }

    @GetMapping("/{meetingId}/attendees")
    @ResponseStatus(HttpStatus.OK)
    public MeetingAttendeesResponse getMeetingAttendees(@PathVariable Long meetingId,
                                    @Valid FindMeetingAttendeesParameters findMeetingAttendeesParameters) {
        MeetingAttendeesResponseDto responseDto = meetingQueryUseCase
                .getMeetingAttendees(findMeetingAttendeesParameters.toRequestDto(meetingId));
        return MeetingAttendeesResponse.fromResponseDto(responseDto);
    }
}
