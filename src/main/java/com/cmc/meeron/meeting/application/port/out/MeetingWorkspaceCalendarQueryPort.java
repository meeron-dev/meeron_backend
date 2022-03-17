package com.cmc.meeron.meeting.application.port.out;

import com.cmc.meeron.meeting.application.port.out.response.MonthMeetingsCountQueryDto;
import com.cmc.meeron.meeting.application.port.out.response.YearMeetingsCountQueryDto;
import com.cmc.meeron.meeting.domain.Meeting;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.util.List;

public interface MeetingWorkspaceCalendarQueryPort {

    List<Integer> findWorkspaceMeetingDays(Long workspaceUserId, YearMonth yearMonth);

    List<Meeting> findWorkspaceDayMeetings(Long workspaceUserId, LocalDate localDate);

    List<YearMeetingsCountQueryDto> findWorkspaceYearMeetingsCount(Long workspaceUserId);

    List<MonthMeetingsCountQueryDto> findWorkspaceMonthMeetingsCount(Long workspaceUserId, Year year);
}
