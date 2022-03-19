package com.cmc.meeron.team.application.port.in;

import com.cmc.meeron.team.application.port.in.request.CreateTeamRequestDto;

public interface TeamCommandUseCase {

    Long createTeam(CreateTeamRequestDto createTeamRequestDto);
}
