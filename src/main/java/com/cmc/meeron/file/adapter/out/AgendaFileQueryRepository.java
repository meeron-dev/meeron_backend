package com.cmc.meeron.file.adapter.out;

import com.cmc.meeron.file.application.port.out.AgendaFileQueryPort;
import com.cmc.meeron.file.domain.AgendaFile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
class AgendaFileQueryRepository implements AgendaFileQueryPort {

    private final AgendaFileJpaRepository agendaFileJpaRepository;

    @Override
    public int countByMeetingId(Long meetingId) {
        return agendaFileJpaRepository.countByMeetingId(meetingId);
    }

    @Override
    public List<AgendaFile> findByAgendaId(Long agendaId) {
        return agendaFileJpaRepository.findByAgendaId(agendaId);
    }
}
