package com.cmc.meeron.topic.issue.application.port.out;

import com.cmc.meeron.topic.issue.domain.Issue;

import java.util.List;

public interface IssueQueryPort {

    List<Issue> findByAgendaId(Long agendaId);
}
