package com.cmc.meeron.meeting.adapter.out;

import com.cmc.meeron.meeting.domain.Agenda;
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
import java.util.Optional;

@Repository
@RequiredArgsConstructor
class MeetingQueryRepository implements MeetingQueryPort {

    private final MeetingJpaRepository meetingJpaRepository;
    private final AgendaJpaRepository agendaJpaRepository;

    @Override
    public List<Meeting> findTodayMeetings(Long workspaceId, Long workspaceUserId) {
        return meetingJpaRepository.findTodayMeetings(workspaceId, workspaceUserId, LocalDate.now());
    }

    @Override
    public List<Integer> findMeetingDays(String searchType, List<Long> searchIds, YearMonth yearMonth) {
        // TODO: 2022/03/17 kobeomseok95 delete
//        return meetingQuerydslRepository.findMeetingDays(searchType, searchIds, yearMonth);
        return null;
    }

    @Override
    public List<Meeting> findDayMeetings(String searchType, List<Long> searchIds, LocalDate date) {
        // TODO: 2022/03/17 kobeomseok95 delete
//        return meetingQuerydslRepository.findDayMeetings(searchType, searchIds, date);
        return null;
    }

    @Override
    public List<YearMeetingsCountQueryDto> findYearMeetingsCount(String searchType, List<Long> searchIds) {
        // TODO: 2022/03/17 kobeomseok95 delete
//        return meetingQuerydslRepository.findYearMeetingsCount(searchType, searchIds);
        return null;
    }

    @Override
    public List<MonthMeetingsCountQueryDto> findMonthMeetingsCount(String searchType, List<Long> searchIds, Year year) {
        // TODO: 2022/03/17 kobeomseok95 delete
//        return meetingQuerydslRepository.findMonthMeetingsCount(searchType, searchIds, year);
        return null;
    }

    @Override
    public Optional<Meeting> findById(Long meetingId) {
        return meetingJpaRepository.findById(meetingId);
    }

    @Override
    public Optional<Agenda> findAgendaById(Long agendaId) {
        return agendaJpaRepository.findById(agendaId);
    }

    @Override
    public Optional<Meeting> findWithAttendeesById(Long meetingId) {
        return meetingJpaRepository.findWithAttendeesById(meetingId);
    }
}
