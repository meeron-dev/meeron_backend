package com.cmc.meeron.meeting.application.service;

import com.cmc.meeron.common.exception.meeting.AgendaNotFoundException;
import com.cmc.meeron.file.application.port.out.AgendaFileQueryPort;
import com.cmc.meeron.file.domain.AgendaFile;
import com.cmc.meeron.meeting.application.port.in.request.FindAgendaIssuesFilesRequestDto;
import com.cmc.meeron.meeting.application.port.in.response.AgendaIssuesFilesResponseDto;
import com.cmc.meeron.meeting.application.port.in.AgendaQueryUseCase;
import com.cmc.meeron.meeting.application.port.in.response.AgendaCountResponseDto;
import com.cmc.meeron.meeting.application.port.out.AgendaQueryPort;
import com.cmc.meeron.meeting.domain.Agenda;
import com.cmc.meeron.meeting.domain.Issue;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class AgendaQueryService implements AgendaQueryUseCase {

    private final AgendaQueryPort agendaQueryPort;
    private final AgendaFileQueryPort agendaFileQueryPort;

    // TODO: 2022/04/01 kobeomseok95 exist가 아닌 count로 변경
    @Override
    public AgendaCountResponseDto getAgendaCountsByMeetingId(Long meetingId) {
        if (!agendaQueryPort.existsByMeetingId(meetingId)) {
            return AgendaCountResponseDto.ofFalse();
        }
        int fileCount = agendaFileQueryPort.countByMeetingId(meetingId);
        return AgendaCountResponseDto.ofTrue(fileCount);
    }

    @Override
    public AgendaIssuesFilesResponseDto getAgendaIssuesFiles(FindAgendaIssuesFilesRequestDto findAgendaIssuesFilesRequestDto) {
        Agenda agenda = agendaQueryPort.findByMeetingIdAndAgendaOrder(findAgendaIssuesFilesRequestDto.getMeetingId(),
                findAgendaIssuesFilesRequestDto.getAgendaOrder())
                .orElseThrow(AgendaNotFoundException::new);
        List<Issue> issues = agendaQueryPort.findByAgendaId(agenda.getId());
        List<AgendaFile> files = agendaFileQueryPort.findByAgendaId(agenda.getId());
        return AgendaIssuesFilesResponseDto.fromEntities(agenda, issues, files);
    }
}
