package com.cmc.meeron.meeting.domain;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public interface MeetingRepository {

    List<Meeting> findTodayMeetings(Long workspaceId, Long workspaceUserId);

    List<Integer> findMeetingDays(String searchType, List<Long> searchIds, YearMonth yearMonth);

    List<Meeting> findDayMeetings(String searchType, List<Long> searchIds, LocalDate date);
}
