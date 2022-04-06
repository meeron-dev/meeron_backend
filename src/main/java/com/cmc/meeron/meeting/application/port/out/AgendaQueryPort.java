package com.cmc.meeron.meeting.application.port.out;

import com.cmc.meeron.meeting.domain.Agenda;
import com.cmc.meeron.meeting.domain.Issue;

import java.util.List;
import java.util.Optional;

public interface AgendaQueryPort {

    long countsByMeetingId(Long meetingId);

    Optional<Agenda> findByMeetingIdAndAgendaOrder(Long meetingId, int agendaOrder);

    List<Issue> findByAgendaId(Long agendaId);

    List<Agenda> findByMeetingIds(List<Long> meetingIds);
}
