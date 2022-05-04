package com.cmc.meeron.file.adapter.out;

import com.cmc.meeron.file.application.port.out.AgendaFileQueryPort;
import com.cmc.meeron.topic.agenda.application.port.out.AgendaToAgendaFileQueryPort;
import com.cmc.meeron.file.domain.AgendaFile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
class AgendaFileQueryRepository implements AgendaToAgendaFileQueryPort,
        AgendaFileQueryPort {

    private final AgendaFileJpaRepository agendaFileJpaRepository;

    @Override
    public long countByMeetingId(Long meetingId) {
        return agendaFileJpaRepository.countByMeetingId(meetingId);
    }

    @Override
    public List<AgendaFile> findByAgendaId(Long agendaId) {
        return agendaFileJpaRepository.findByAgendaId(agendaId);
    }
}
