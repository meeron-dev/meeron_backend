package com.cmc.meeron.file.application.port.out;

import com.cmc.meeron.file.domain.AgendaFile;

import java.util.List;

public interface AgendaFileQueryPort {

    long countByMeetingId(Long meetingId);

    List<AgendaFile> findByAgendaId(Long agendaId);
}
