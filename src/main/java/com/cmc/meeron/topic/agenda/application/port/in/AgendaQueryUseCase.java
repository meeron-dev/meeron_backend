package com.cmc.meeron.topic.agenda.application.port.in;

import com.cmc.meeron.topic.agenda.application.port.in.request.FindAgendaIssuesFilesRequestDto;
import com.cmc.meeron.topic.agenda.application.port.in.response.AgendaIssuesFilesResponseDto;
import com.cmc.meeron.topic.agenda.application.port.in.response.AgendaCountResponseDto;

public interface AgendaQueryUseCase {

    AgendaCountResponseDto getAgendaCountsByMeetingId(Long meetingId);

    AgendaIssuesFilesResponseDto getAgendaIssuesFiles(FindAgendaIssuesFilesRequestDto findAgendaIssuesFilesRequestDto);
}
