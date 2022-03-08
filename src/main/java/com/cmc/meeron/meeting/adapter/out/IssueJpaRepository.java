package com.cmc.meeron.meeting.adapter.out;

import com.cmc.meeron.meeting.domain.Issue;
import org.springframework.data.jpa.repository.JpaRepository;

interface IssueJpaRepository extends JpaRepository<Issue, Long> {
}
