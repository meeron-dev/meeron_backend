package com.cmc.meeron.meeting.adapter.in;

import com.cmc.meeron.meeting.adapter.in.request.*;
import com.cmc.meeron.meeting.adapter.in.response.*;
import com.cmc.meeron.meeting.application.port.in.MeetingQueryUseCase;
import com.cmc.meeron.meeting.application.port.in.response.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/meetings")
public class MeetingQueryRestController {

    private final MeetingQueryUseCase meetingQueryUseCase;

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
        List<Integer> days = meetingQueryUseCase.getMeetingDays(meetingDaysRequest.toRequestDto());
        return MeetingDaysResponse.of(days);
    }

    // FIXME: 2022/03/02 kobeomseok95 refactoring
    @GetMapping("/day")
    @ResponseStatus(HttpStatus.OK)
    public DayMeetingsResponse getDayMeetings(@Valid DayMeetingsRequest dayMeetingsRequest) {
        if (!isWorkspaceUserType(dayMeetingsRequest)) {
            List<WorkspaceAndTeamDayMeetingResponseDto> workspaceAndTeamDayMeetingsResponseDtos =
                    meetingQueryUseCase.getWorkspaceAndTeamDayMeetings(dayMeetingsRequest.toRequestDto());
            return DayMeetingsResponse.fromWorkspaceAndTeam(workspaceAndTeamDayMeetingsResponseDtos);
        }

        List<WorkspaceUserDayMeetingResponseDto> workspaceUserDayMeetingsResponseDtos =
                meetingQueryUseCase.getWorkspaceUserDayMeetings(dayMeetingsRequest.toRequestDto());
        return DayMeetingsResponse.fromWorkspaceUser(workspaceUserDayMeetingsResponseDtos);
    }

    private boolean isWorkspaceUserType(DayMeetingsRequest dayMeetingsRequest) {
        return dayMeetingsRequest.getType().equals(MeetingDaysSearchType.WORKSPACE_USER.name());
    }

    @GetMapping("/years")
    @ResponseStatus(HttpStatus.OK)
    public YearMeetingsCountResponse getYearMeetingsCount(@Valid YearMeetingsCountRequest yearMeetingsCountRequest) {
        List<YearMeetingsCountResponseDto> yearMeetingsCount =
                meetingQueryUseCase.getYearMeetingsCount(yearMeetingsCountRequest.toRequestDto());
        return YearMeetingsCountResponse.of(yearMeetingsCount);
    }

    @GetMapping("/months")
    @ResponseStatus(HttpStatus.OK)
    public MonthMeetingsCountResponse getMonthMeetingsCount(@Valid MonthMeetingsCountRequest monthMeetingsCountRequest) {
        List<MonthMeetingsCountResponseDto> monthMeetingsCount =
                meetingQueryUseCase.getMonthMeetingsCount(monthMeetingsCountRequest.toRequestDto());
        return MonthMeetingsCountResponse.of(monthMeetingsCount);
    }
}
