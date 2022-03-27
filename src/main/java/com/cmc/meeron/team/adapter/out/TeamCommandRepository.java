package com.cmc.meeron.team.adapter.out;

import com.cmc.meeron.team.application.port.out.TeamCommandPort;
import com.cmc.meeron.team.domain.Team;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
class TeamCommandRepository implements TeamCommandPort {

    private final TeamJpaRepository teamJpaRepository;

    @Override
    public Long save(Team team) {
        return teamJpaRepository.save(team).getId();
    }

    @Override
    public void deleteById(Long teamId) {
        teamJpaRepository.deleteById(teamId);
    }
}
