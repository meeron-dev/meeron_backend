package com.cmc.meeron.topic.agenda.application.port.out;

import com.cmc.meeron.file.domain.AgendaFile;

import java.util.List;

public interface AgendaToAgendaFileQueryPort {

    long countByMeetingId(Long meetingId);

    List<AgendaFile> findByAgendaId(Long agendaId);
}
