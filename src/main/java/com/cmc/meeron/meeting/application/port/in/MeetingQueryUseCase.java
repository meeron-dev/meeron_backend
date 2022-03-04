package com.cmc.meeron.meeting.application.port.in;

import com.cmc.meeron.meeting.application.port.in.request.*;
import com.cmc.meeron.meeting.application.port.in.response.*;

import java.util.List;

public interface MeetingQueryUseCase {

    List<TodayMeetingResponseDto> getTodayMeetings(TodayMeetingRequestDto todayMeetingRequestDto);

    List<Integer> getMeetingDays(MeetingDaysRequestDto meetingDaysRequestDto);

    List<WorkspaceAndTeamDayMeetingResponseDto> getWorkspaceAndTeamDayMeetings(DayMeetingsRequestDto dayMeetingsRequestDto);

    List<WorkspaceUserDayMeetingResponseDto> getWorkspaceUserDayMeetings(DayMeetingsRequestDto dayMeetingsRequestDto);

    List<YearMeetingsCountResponseDto> getYearMeetingsCount(MeetingSearchRequestDto meetingSearchRequestDto);

    List<MonthMeetingsCountResponseDto> getMonthMeetingsCount(MonthMeetingsCountRequestDto monthMeetingsCountRequestDto);
}
