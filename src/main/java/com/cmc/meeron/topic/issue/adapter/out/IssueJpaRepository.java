package com.cmc.meeron.topic.issue.adapter.out;

import com.cmc.meeron.topic.issue.domain.Issue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface IssueJpaRepository extends JpaRepository<Issue, Long> {

    List<Issue> findByAgendaId(Long agendaId);
}
