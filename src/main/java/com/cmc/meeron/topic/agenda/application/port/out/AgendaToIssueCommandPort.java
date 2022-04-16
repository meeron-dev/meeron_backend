package com.cmc.meeron.topic.agenda.application.port.out;

import com.cmc.meeron.topic.issue.domain.Issue;

import java.util.List;

public interface AgendaToIssueCommandPort {

    void save(List<Issue> issues);
}
