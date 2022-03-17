package com.cmc.meeron.meeting.application.port.out;

import com.cmc.meeron.meeting.application.port.out.response.MonthMeetingsCountQueryDto;
import com.cmc.meeron.meeting.application.port.out.response.YearMeetingsCountQueryDto;
import com.cmc.meeron.meeting.domain.Meeting;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.util.List;

public interface MeetingTeamCalendarQueryPort {

    List<Integer> findTeamMeetingDays(Long teamId, YearMonth yearMonth);

    List<Meeting> findTeamDayMeetings(Long teamId, LocalDate localDate);

    List<YearMeetingsCountQueryDto> findTeamYearMeetingsCount(Long teamId);

    List<MonthMeetingsCountQueryDto> findTeamMonthMeetingsCount(Long teamId, Year year);
}
