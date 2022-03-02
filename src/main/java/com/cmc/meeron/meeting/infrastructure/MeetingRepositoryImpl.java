package com.cmc.meeron.meeting.infrastructure;

import com.cmc.meeron.meeting.domain.Meeting;
import com.cmc.meeron.meeting.domain.MeetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Repository
@RequiredArgsConstructor
class MeetingRepositoryImpl implements MeetingRepository {

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
}
