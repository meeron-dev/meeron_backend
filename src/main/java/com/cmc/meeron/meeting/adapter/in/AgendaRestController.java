package com.cmc.meeron.meeting.adapter.in;

import com.cmc.meeron.meeting.adapter.in.response.AgendaCountResponse;
import com.cmc.meeron.meeting.adapter.in.response.AgendaIssuesFilesResponse;
import com.cmc.meeron.meeting.application.port.in.AgendaQueryUseCase;
import com.cmc.meeron.meeting.application.port.in.request.FindAgendaIssuesFilesRequestDto;
import com.cmc.meeron.meeting.application.port.in.response.AgendaCountResponseDto;
import com.cmc.meeron.meeting.application.port.in.response.AgendaIssuesFilesResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AgendaRestController {

    private final AgendaQueryUseCase agendaQueryUseCase;

    @GetMapping("/meetings/{meetingId}/agendas/count")
    @ResponseStatus(HttpStatus.OK)
    public AgendaCountResponse getAgendaCounts(@PathVariable Long meetingId) {
        AgendaCountResponseDto responseDto = agendaQueryUseCase.getAgendaCountsByMeetingId(meetingId);
        return AgendaCountResponse.fromResponseDto(responseDto);
    }

    @GetMapping("/meetings/{meetingId}/agendas/{agendaOrder}")
    @ResponseStatus(HttpStatus.OK)
    public AgendaIssuesFilesResponse getAgendaIssuesFiles(@PathVariable Long meetingId,
                                                    @PathVariable int agendaOrder) {
        AgendaIssuesFilesResponseDto agendaIssuesFilesResponseDto = agendaQueryUseCase
                .getAgendaIssuesFiles(FindAgendaIssuesFilesRequestDto.of(meetingId, agendaOrder));
        return AgendaIssuesFilesResponse.fromResponseDto(agendaIssuesFilesResponseDto);
    }
}
