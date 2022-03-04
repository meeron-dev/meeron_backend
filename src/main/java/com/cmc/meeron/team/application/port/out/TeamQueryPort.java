package com.cmc.meeron.team.application.port.out;

import com.cmc.meeron.team.application.port.out.response.WorkspaceTeamsQueryResponseDto;

import java.util.List;

public interface TeamQueryPort {

    List<WorkspaceTeamsQueryResponseDto> findByWorkspaceId(Long workspaceId);
}
