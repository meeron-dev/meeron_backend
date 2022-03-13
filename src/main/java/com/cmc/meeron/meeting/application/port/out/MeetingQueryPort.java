package com.cmc.meeron.meeting.application.port.out;

import com.cmc.meeron.meeting.domain.Agenda;
import com.cmc.meeron.meeting.domain.Meeting;
import com.cmc.meeron.meeting.application.port.out.response.MonthMeetingsCountQueryDto;
import com.cmc.meeron.meeting.application.port.out.response.YearMeetingsCountQueryDto;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

public interface MeetingQueryPort {

    List<Meeting> findTodayMeetings(Long workspaceId, Long workspaceUserId);

    List<Integer> findMeetingDays(String searchType, List<Long> searchIds, YearMonth yearMonth);

    List<Meeting> findDayMeetings(String searchType, List<Long> searchIds, LocalDate date);

    List<YearMeetingsCountQueryDto> findYearMeetingsCount(String searchType, List<Long> searchIds);

    List<MonthMeetingsCountQueryDto> findMonthMeetingsCount(String searchType, List<Long> searchIds, Year year);

    Optional<Meeting> findById(Long meetingId);

    Optional<Agenda> findAgendaById(Long agendaId);

    Optional<Meeting> findWithAttendeesById(Long meetingId);
}
