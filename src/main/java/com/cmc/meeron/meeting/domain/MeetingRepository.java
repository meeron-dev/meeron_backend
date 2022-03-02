package com.cmc.meeron.meeting.domain;

import com.cmc.meeron.meeting.domain.dto.MonthMeetingsCountDto;
import com.cmc.meeron.meeting.domain.dto.YearMeetingsCountDto;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.util.List;

public interface MeetingRepository {

    List<Meeting> findTodayMeetings(Long workspaceId, Long workspaceUserId);

    List<Integer> findMeetingDays(String searchType, List<Long> searchIds, YearMonth yearMonth);

    List<Meeting> findDayMeetings(String searchType, List<Long> searchIds, LocalDate date);

    List<YearMeetingsCountDto> findYearMeetingsCount(String searchType, List<Long> searchIds);

    List<MonthMeetingsCountDto> findMonthMeetingsCount(String searchType, List<Long> searchIds, Year year);
}
