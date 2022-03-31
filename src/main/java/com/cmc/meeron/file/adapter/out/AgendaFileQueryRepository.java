package com.cmc.meeron.file.adapter.out;

import com.cmc.meeron.file.application.port.out.AgendaFileQueryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
class AgendaFileQueryRepository implements AgendaFileQueryPort {

    private final AgendaFileJpaRepository agendaFileJpaRepository;

    @Override
    public int countByMeetingId(Long meetingId) {
        return agendaFileJpaRepository.countByMeetingId(meetingId);
    }
}
