package com.cmc.meeron.topic.agenda.application.port.out;

import com.cmc.meeron.topic.issue.domain.Issue;

import java.util.List;

public interface AgendaToIssueQueryPort {

    List<Issue> findByAgendaId(Long agendaId);
}
