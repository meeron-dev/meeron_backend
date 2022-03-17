package com.cmc.meeron.meeting.application.port.out;

import com.cmc.meeron.meeting.application.port.out.response.MonthMeetingsCountQueryDto;
import com.cmc.meeron.meeting.application.port.out.response.YearMeetingsCountQueryDto;
import com.cmc.meeron.meeting.domain.Meeting;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.util.List;

public interface MeetingMyCalendarQueryPort {

    List<Integer> findMyMeetingDays(List<Long> myWorkspaceUserIds, YearMonth yearMonth);

    List<Meeting> findMyDayMeetings(List<Long> myWorkspaceUserIds, LocalDate localDate);

    List<YearMeetingsCountQueryDto> findMyYearMeetingsCount(List<Long> myWorkspaceUserIds);

    List<MonthMeetingsCountQueryDto> findMyMonthMeetingsCount(List<Long> myWorkspaceUserIds, Year year);
}
