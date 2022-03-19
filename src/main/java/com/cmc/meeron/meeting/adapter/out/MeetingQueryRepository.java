package com.cmc.meeron.meeting.adapter.out;

import com.cmc.meeron.meeting.application.port.out.MeetingQueryPort;
import com.cmc.meeron.meeting.domain.Agenda;
import com.cmc.meeron.meeting.domain.Meeting;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
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
