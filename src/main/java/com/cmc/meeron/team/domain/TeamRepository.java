package com.cmc.meeron.team.domain;

import com.cmc.meeron.team.domain.dto.WorkspaceTeamsQueryResponseDto;

import java.util.List;

public interface TeamRepository {

    List<WorkspaceTeamsQueryResponseDto> findByWorkspaceId(Long workspaceId);
}
