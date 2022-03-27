package com.cmc.meeron.team.application.port.out;

import com.cmc.meeron.team.domain.Team;

public interface TeamCommandPort {

    Long save(Team team);

    void deleteById(Long teamId);
}
