package com.cmc.meeron.topic.agenda.adapter.in;

import com.cmc.meeron.topic.agenda.adapter.in.response.CreateAgendaResponse;
import com.cmc.meeron.topic.agenda.adapter.in.request.CreateAgendaRequest;
import com.cmc.meeron.topic.agenda.adapter.in.response.AgendaCountResponse;
import com.cmc.meeron.topic.agenda.adapter.in.response.AgendaIssuesFilesResponse;
import com.cmc.meeron.topic.agenda.application.port.in.AgendaCommandUseCase;
import com.cmc.meeron.topic.agenda.application.port.in.AgendaQueryUseCase;
import com.cmc.meeron.topic.agenda.application.port.in.request.FindAgendaIssuesFilesRequestDto;
import com.cmc.meeron.topic.agenda.application.port.in.response.AgendaCountResponseDto;
import com.cmc.meeron.topic.agenda.application.port.in.response.AgendaIssuesFilesResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AgendaRestController {

    private final AgendaQueryUseCase agendaQueryUseCase;
    private final AgendaCommandUseCase agendaCommandUseCase;

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

    @PostMapping("/meetings/{meetingId}/agendas")
    @ResponseStatus(HttpStatus.CREATED)
    public CreateAgendaResponse createAgendas(@PathVariable Long meetingId,
                                              @RequestBody @Valid CreateAgendaRequest createAgendaRequest) {
        List<Long> responseDtos =
                agendaCommandUseCase.createAgendas(createAgendaRequest.toRequestDtoAndSortByAgendaOrder(meetingId));
        return CreateAgendaResponse.of(responseDtos);
    }
}
