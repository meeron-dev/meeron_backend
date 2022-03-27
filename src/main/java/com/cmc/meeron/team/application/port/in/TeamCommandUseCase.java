package com.cmc.meeron.team.application.port.in;

import com.cmc.meeron.team.application.port.in.request.CreateTeamRequestDto;
import com.cmc.meeron.team.application.port.in.request.DeleteTeamRequestDto;
import com.cmc.meeron.team.application.port.in.request.ModifyTeamNameRequestDto;

public interface TeamCommandUseCase {

    Long createTeam(CreateTeamRequestDto createTeamRequestDto);

    void deleteTeam(DeleteTeamRequestDto deleteTeamRequestDto);

    void modifyTeamName(ModifyTeamNameRequestDto modifyTeamNameRequestDto);
}
