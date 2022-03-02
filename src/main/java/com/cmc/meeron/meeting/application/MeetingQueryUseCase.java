package com.cmc.meeron.meeting.application;

import com.cmc.meeron.meeting.application.dto.request.*;
import com.cmc.meeron.meeting.application.dto.response.*;

import java.util.List;

public interface MeetingQueryUseCase {

    List<TodayMeetingResponseDto> getTodayMeetings(TodayMeetingRequestDto todayMeetingRequestDto);

    List<Integer> getMeetingDays(MeetingDaysRequestDto meetingDaysRequestDto);

    List<WorkspaceAndTeamDayMeetingResponseDto> getWorkspaceAndTeamDayMeetings(DayMeetingsRequestDto dayMeetingsRequestDto);

    List<WorkspaceUserDayMeetingResponseDto> getWorkspaceUserDayMeetings(DayMeetingsRequestDto dayMeetingsRequestDto);

    List<YearMeetingsCountResponseDto> getYearMeetingsCount(MeetingSearchRequestDto meetingSearchRequestDto);

    List<MonthMeetingsCountResponseDto> getMonthMeetingsCount(MonthMeetingsCountRequestDto monthMeetingsCountRequestDto);
}
