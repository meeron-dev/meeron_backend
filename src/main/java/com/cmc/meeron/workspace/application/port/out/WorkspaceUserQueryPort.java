package com.cmc.meeron.workspace.application.port.out;

import com.cmc.meeron.workspace.application.port.out.response.WorkspaceUserQueryResponseDto;
import com.cmc.meeron.workspace.domain.WorkspaceUser;

import java.util.List;
import java.util.Optional;

public interface WorkspaceUserQueryPort {

    List<WorkspaceUser> findMyWorkspaceUsers(Long userId);

    Optional<WorkspaceUser> findWorkspaceUserById(Long workspaceUserId);

    List<WorkspaceUserQueryResponseDto> findByWorkspaceIdNickname(Long workspaceId, String nickname);

    List<WorkspaceUserQueryResponseDto> findByTeamId(Long teamId);

    List<WorkspaceUser> findAllWorkspaceUsersByIds(List<Long> workspaceUserIds);

    Optional<WorkspaceUser> findByUserWorkspaceId(Long userId, Long workspaceId);

    boolean existsByNicknameInWorkspace(Long workspaceId, String nickname);
}
