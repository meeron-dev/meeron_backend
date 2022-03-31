package com.cmc.meeron.meeting.adapter.out;

import com.cmc.meeron.meeting.application.port.out.AgendaQueryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
class AgendaQueryRepository implements AgendaQueryPort {

    private final AgendaJpaRepository agendaJpaRepository;

    @Override
    public boolean existsByMeetingId(Long meetingId) {
        return agendaJpaRepository.existsByMeetingId(meetingId);
    }
}
