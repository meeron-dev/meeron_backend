package com.cmc.meeron.topic.agenda.adapter.out;

import com.cmc.meeron.file.application.port.out.AgendaFileToAgendaQueryPort;
import com.cmc.meeron.meeting.application.port.out.MeetingToAgendaQueryPort;
import com.cmc.meeron.topic.agenda.application.port.out.AgendaCommandPort;
import com.cmc.meeron.topic.agenda.application.port.out.AgendaQueryPort;
import com.cmc.meeron.topic.agenda.domain.Agenda;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
class AgendaRepository implements AgendaQueryPort,
        AgendaFileToAgendaQueryPort,
        MeetingToAgendaQueryPort,
        AgendaCommandPort {

    private final AgendaJpaRepository agendaJpaRepository;

    @Override
    public long countsByMeetingId(Long meetingId) {
        return agendaJpaRepository.countsByMeetingId(meetingId);
    }

    @Override
    public Optional<Agenda> findByMeetingIdAndAgendaOrder(Long meetingId, int agendaOrder) {
        return agendaJpaRepository.findByMeetingIdAndAgendaOrder(meetingId, agendaOrder);
    }

    @Override
    public List<Agenda> findByMeetingId(Long meetingId) {
        return agendaJpaRepository.findByMeetingId(meetingId);
    }

    @Override
    public List<Agenda> findByMeetingIds(List<Long> meetingIds) {
        return agendaJpaRepository.findByMeetingIds(meetingIds);
    }

    @Override
    public Optional<Agenda> findById(Long agendaId) {
        return agendaJpaRepository.findById(agendaId);
    }

    @Override
    public Agenda save(Agenda agenda) {
        return agendaJpaRepository.save(agenda);
    }
}
