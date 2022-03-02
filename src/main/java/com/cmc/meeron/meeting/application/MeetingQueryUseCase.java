package com.cmc.meeron.meeting.application;

import com.cmc.meeron.meeting.application.dto.request.DayMeetingsRequestDto;
import com.cmc.meeron.meeting.application.dto.request.MeetingDaysRequestDto;
import com.cmc.meeron.meeting.application.dto.request.TodayMeetingRequestDto;
import com.cmc.meeron.meeting.application.dto.response.WorkspaceAndTeamDayMeetingResponseDto;
import com.cmc.meeron.meeting.application.dto.response.TodayMeetingResponseDto;
import com.cmc.meeron.meeting.application.dto.response.WorkspaceUserDayMeetingResponseDto;

import java.util.List;

public interface MeetingQueryUseCase {

    List<TodayMeetingResponseDto> getTodayMeetings(TodayMeetingRequestDto todayMeetingRequestDto);

    List<Integer> getMeetingDays(MeetingDaysRequestDto meetingDaysRequestDto);

    List<WorkspaceAndTeamDayMeetingResponseDto> getWorkspaceAndTeamDayMeetings(DayMeetingsRequestDto dayMeetingsRequestDto);

    List<WorkspaceUserDayMeetingResponseDto> getWorkspaceUserDayMeetings(DayMeetingsRequestDto dayMeetingsRequestDto);
}
