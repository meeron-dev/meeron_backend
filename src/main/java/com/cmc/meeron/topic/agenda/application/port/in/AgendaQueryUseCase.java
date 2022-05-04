package com.cmc.meeron.topic.agenda.application.port.in;

import com.cmc.meeron.topic.agenda.application.port.in.request.FindAgendaIssuesFilesRequestDto;
import com.cmc.meeron.topic.agenda.application.port.in.response.AgendaCountResponseDto;
import com.cmc.meeron.topic.agenda.application.port.in.response.AgendaIssuesFilesResponseDto;
import com.cmc.meeron.topic.agenda.application.port.in.response.AgendaResponseDto;

import java.util.List;

public interface AgendaQueryUseCase {

    AgendaCountResponseDto getAgendaCountsByMeetingId(Long meetingId);

    @Deprecated
    AgendaIssuesFilesResponseDto getAgendaIssuesFiles(FindAgendaIssuesFilesRequestDto findAgendaIssuesFilesRequestDto);

    List<AgendaResponseDto> getMeetingAgendas(Long meetingId);

    AgendaResponseDto getAgenda(Long agendaId);
}
