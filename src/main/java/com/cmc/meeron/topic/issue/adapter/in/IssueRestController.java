package com.cmc.meeron.topic.issue.adapter.in;

import com.cmc.meeron.topic.issue.adapter.in.response.IssueResponses;
import com.cmc.meeron.topic.issue.application.port.in.IssueQueryUseCase;
import com.cmc.meeron.topic.issue.application.port.in.response.IssueResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class IssueRestController {

    private final IssueQueryUseCase issueQueryUseCase;

    @GetMapping("/agendas/{agendaId}/issues")
    @ResponseStatus(HttpStatus.OK)
    public IssueResponses getAgendaIssues(@PathVariable Long agendaId) {
        List<IssueResponseDto> responseDtos = issueQueryUseCase.getAgendaIssues(agendaId);
        return IssueResponses.from(responseDtos);
    }
}
