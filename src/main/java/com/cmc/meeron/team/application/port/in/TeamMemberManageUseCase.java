package com.cmc.meeron.team.application.port.in;

import com.cmc.meeron.team.application.port.in.request.JoinTeamMembersRequestDto;
import com.cmc.meeron.team.application.port.in.request.EjectTeamMemberRequestDto;

public interface TeamMemberManageUseCase {

    void joinTeamMembers(JoinTeamMembersRequestDto joinTeamMembersRequestDto);

    void ejectTeamMember(EjectTeamMemberRequestDto ejectTeamMemberRequestDto);
}
