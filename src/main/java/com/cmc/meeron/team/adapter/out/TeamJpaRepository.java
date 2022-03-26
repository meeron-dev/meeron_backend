package com.cmc.meeron.team.adapter.out;

import com.cmc.meeron.team.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;

interface TeamJpaRepository extends JpaRepository<Team, Long> {

    Long countByWorkspaceId(Long workspaceId);
}
