package com.cmc.meeron.file.application.port.out;

import com.cmc.meeron.file.domain.AgendaFile;

public interface AgendaFileCommandPort {

    void save(AgendaFile agendaFile);
}
