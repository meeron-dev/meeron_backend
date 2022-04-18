package com.cmc.meeron.file.application.port.out;

import com.cmc.meeron.topic.agenda.domain.Agenda;

import java.util.Optional;

public interface AgendaFileToAgendaQueryPort {

    Optional<Agenda> findById(Long agendaId);
}
