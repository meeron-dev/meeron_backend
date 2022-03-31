package com.cmc.meeron.meeting.adapter.in;

import com.cmc.meeron.meeting.adapter.in.response.AgendaCountResponse;
import com.cmc.meeron.meeting.application.port.in.AgendaQueryUseCase;
import com.cmc.meeron.meeting.application.port.in.response.AgendaCountResponseDto;
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
}
