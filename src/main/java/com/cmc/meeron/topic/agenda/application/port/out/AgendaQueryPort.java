package com.cmc.meeron.topic.agenda.application.port.out;

import com.cmc.meeron.topic.agenda.domain.Agenda;

import java.util.List;
import java.util.Optional;

public interface AgendaQueryPort {

    long countsByMeetingId(Long meetingId);

    Optional<Agenda> findByMeetingIdAndAgendaOrder(Long meetingId, int agendaOrder);

    List<Agenda> findByMeetingId(Long meetingId);

    Optional<Agenda> findById(Long agendaId);
}
