package com.cmc.meeron.topic.agenda.application.service;

import com.cmc.meeron.common.exception.meeting.AgendaNotFoundException;
import com.cmc.meeron.file.domain.AgendaFile;
import com.cmc.meeron.topic.agenda.application.port.in.AgendaQueryUseCase;
import com.cmc.meeron.topic.agenda.application.port.in.request.FindAgendaIssuesFilesRequestDto;
import com.cmc.meeron.topic.agenda.application.port.in.response.AgendaCountResponseDto;
import com.cmc.meeron.topic.agenda.application.port.in.response.AgendaIssuesFilesResponseDto;
import com.cmc.meeron.topic.agenda.application.port.out.AgendaToAgendaFileQueryPort;
import com.cmc.meeron.topic.agenda.application.port.out.AgendaQueryPort;
import com.cmc.meeron.topic.agenda.application.port.out.AgendaToIssueQueryPort;
import com.cmc.meeron.topic.agenda.domain.Agenda;
import com.cmc.meeron.topic.issue.domain.Issue;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class AgendaQueryService implements AgendaQueryUseCase {

    private final AgendaQueryPort agendaQueryPort;
    private final AgendaToAgendaFileQueryPort agendaToAgendaFileQueryPort;
    private final AgendaToIssueQueryPort agendaToIssueQueryPort;

    @Override
    public AgendaCountResponseDto getAgendaCountsByMeetingId(Long meetingId) {
        long agendaCount = agendaQueryPort.countsByMeetingId(meetingId);
        if (agendaCount <= 0)
            return AgendaCountResponseDto.notFound();
        long fileCount = agendaToAgendaFileQueryPort.countByMeetingId(meetingId);
        return AgendaCountResponseDto.found(agendaCount, fileCount);
    }

    @Override
    public AgendaIssuesFilesResponseDto getAgendaIssuesFiles(FindAgendaIssuesFilesRequestDto findAgendaIssuesFilesRequestDto) {
        Agenda agenda = agendaQueryPort.findByMeetingIdAndAgendaOrder(findAgendaIssuesFilesRequestDto.getMeetingId(),
                findAgendaIssuesFilesRequestDto.getAgendaOrder())
                .orElseThrow(AgendaNotFoundException::new);
        List<Issue> issues = agendaToIssueQueryPort.findByAgendaId(agenda.getId());
        List<AgendaFile> files = agendaToAgendaFileQueryPort.findByAgendaId(agenda.getId());
        return AgendaIssuesFilesResponseDto.fromEntities(agenda, issues, files);
    }
}
