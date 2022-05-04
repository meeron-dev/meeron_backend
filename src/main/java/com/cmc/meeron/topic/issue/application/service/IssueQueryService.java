package com.cmc.meeron.topic.issue.application.service;

import com.cmc.meeron.topic.issue.application.port.in.IssueQueryUseCase;
import com.cmc.meeron.topic.issue.application.port.in.response.IssueResponseDto;
import com.cmc.meeron.topic.issue.application.port.out.IssueQueryPort;
import com.cmc.meeron.topic.issue.domain.Issue;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class IssueQueryService implements IssueQueryUseCase {

    private final IssueQueryPort issueQueryPort;

    @Override
    public List<IssueResponseDto> getAgendaIssues(Long agendaId) {
        List<Issue> issues = issueQueryPort.findByAgendaId(agendaId);
        return IssueResponseDto.from(issues);
    }
}
