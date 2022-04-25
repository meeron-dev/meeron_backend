package com.cmc.meeron.topic.issue.adapter.out;

import com.cmc.meeron.topic.agenda.application.port.out.AgendaToIssueCommandPort;
import com.cmc.meeron.topic.agenda.application.port.out.AgendaToIssueQueryPort;
import com.cmc.meeron.topic.issue.application.port.out.IssueQueryPort;
import com.cmc.meeron.topic.issue.domain.Issue;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
class IssueRepository implements AgendaToIssueQueryPort,
        AgendaToIssueCommandPort,
        IssueQueryPort {

    private final IssueJpaRepository issueJpaRepository;

    @Override
    public List<Issue> findByAgendaId(Long agendaId) {
        return issueJpaRepository.findByAgendaId(agendaId);
    }

    @Override
    public void save(List<Issue> issues) {
        issueJpaRepository.saveAll(issues);
    }
}
