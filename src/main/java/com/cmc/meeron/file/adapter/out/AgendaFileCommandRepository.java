package com.cmc.meeron.file.adapter.out;

import com.cmc.meeron.file.application.port.out.AgendaFileCommandPort;
import com.cmc.meeron.file.domain.AgendaFile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
class AgendaFileCommandRepository implements AgendaFileCommandPort {

    private final AgendaFileJpaRepository agendaFileJpaRepository;

    @Override
    public void save(AgendaFile agendaFile) {
        agendaFileJpaRepository.save(agendaFile);
    }
}
