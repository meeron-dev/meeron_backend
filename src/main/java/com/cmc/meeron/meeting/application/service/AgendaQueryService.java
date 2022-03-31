package com.cmc.meeron.meeting.application.service;

import com.cmc.meeron.file.application.port.out.AgendaFileQueryPort;
import com.cmc.meeron.meeting.application.port.in.AgendaQueryUseCase;
import com.cmc.meeron.meeting.application.port.in.response.AgendaCountResponseDto;
import com.cmc.meeron.meeting.application.port.out.AgendaQueryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class AgendaQueryService implements AgendaQueryUseCase {

    private final AgendaQueryPort agendaQueryPort;
    private final AgendaFileQueryPort agendaFileQueryPort;

    @Override
    public AgendaCountResponseDto getAgendaCountsByMeetingId(Long meetingId) {
        if (!agendaQueryPort.existsByMeetingId(meetingId)) {
            return AgendaCountResponseDto.ofFalse();
        }
        int fileCount = agendaFileQueryPort.countByMeetingId(meetingId);
        return AgendaCountResponseDto.ofTrue(fileCount);
    }
}
