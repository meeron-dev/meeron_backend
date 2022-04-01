package com.cmc.meeron.meeting.adapter.out;

import com.cmc.meeron.meeting.application.port.out.AgendaQueryPort;
import com.cmc.meeron.meeting.domain.Agenda;
import com.cmc.meeron.meeting.domain.Issue;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
class AgendaQueryRepository implements AgendaQueryPort {

    private final AgendaJpaRepository agendaJpaRepository;
    private final IssueJpaRepository issueJpaRepository;

    @Override
    public boolean existsByMeetingId(Long meetingId) {
        return agendaJpaRepository.existsByMeetingId(meetingId);
    }

    @Override
    public Optional<Agenda> findByMeetingIdAndAgendaOrder(Long meetingId, int agendaOrder) {
        return agendaJpaRepository.findByMeetingIdAndAgendaOrder(meetingId, agendaOrder);
    }

    @Override
    public List<Issue> findByAgendaId(Long agendaId) {
        return issueJpaRepository.findByAgendaId(agendaId);
    }
}
