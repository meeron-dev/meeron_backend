package com.cmc.meeron.meeting.adapter.out;

import com.cmc.meeron.meeting.domain.Agenda;
import org.springframework.data.jpa.repository.JpaRepository;

interface AgendaJpaRepository extends JpaRepository<Agenda, Long> {

    boolean existsByMeetingId(Long meetingId);
}
