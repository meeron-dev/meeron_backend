package com.cmc.meeron.meeting.adapter.out;

import com.cmc.meeron.meeting.domain.Agenda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface AgendaJpaRepository extends JpaRepository<Agenda, Long> {

    boolean existsByMeetingId(Long meetingId);

    Optional<Agenda> findByMeetingIdAndAgendaOrder(Long meetingId, int agendaOrder);
}
