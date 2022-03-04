package com.cmc.meeron.meeting.adapter.out;

import com.cmc.meeron.meeting.domain.Meeting;
import com.cmc.meeron.meeting.application.port.out.MeetingQueryPort;
import com.cmc.meeron.meeting.application.port.out.response.MonthMeetingsCountQueryDto;
import com.cmc.meeron.meeting.application.port.out.response.YearMeetingsCountQueryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.util.List;

@Repository
@RequiredArgsConstructor
class MeetingQueryRepository implements MeetingQueryPort {

    private final MeetingJpaRepository meetingJpaRepository;
    private final MeetingQuerydslRepository meetingQuerydslRepository;

    @Override
    public List<Meeting> findTodayMeetings(Long workspaceId, Long workspaceUserId) {
        return meetingJpaRepository.findTodayMeetings(workspaceId, workspaceUserId, LocalDate.now());
    }

    @Override
    public List<Integer> findMeetingDays(String searchType, List<Long> searchIds, YearMonth yearMonth) {
        return meetingQuerydslRepository.findMeetingDays(searchType, searchIds, yearMonth);
    }

    @Override
    public List<Meeting> findDayMeetings(String searchType, List<Long> searchIds, LocalDate date) {
        return meetingQuerydslRepository.findDayMeetings(searchType, searchIds, date);
    }

    @Override
    public List<YearMeetingsCountQueryDto> findYearMeetingsCount(String searchType, List<Long> searchIds) {
        return meetingQuerydslRepository.findYearMeetingsCount(searchType, searchIds);
    }

    @Override
    public List<MonthMeetingsCountQueryDto> findMonthMeetingsCount(String searchType, List<Long> searchIds, Year year) {
        return meetingQuerydslRepository.findMonthMeetingsCount(searchType, searchIds, year);
    }
}
