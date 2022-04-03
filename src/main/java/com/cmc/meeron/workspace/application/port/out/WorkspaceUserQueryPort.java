package com.cmc.meeron.workspace.application.port.out;

import com.cmc.meeron.workspace.application.port.out.response.WorkspaceUserQuerydslResponseDto;
import com.cmc.meeron.workspace.domain.WorkspaceUser;

import java.util.List;
import java.util.Optional;

public interface WorkspaceUserQueryPort {

    List<WorkspaceUser> findMyWorkspaceUsers(Long userId);

    Optional<WorkspaceUser> findById(Long workspaceUserId);

    List<WorkspaceUserQuerydslResponseDto> findByWorkspaceIdNickname(Long workspaceId, String nickname);

    List<WorkspaceUserQuerydslResponseDto> findQueryByTeamId(Long teamId);

    List<WorkspaceUser> findByTeamId(Long teamId);

    List<WorkspaceUser> findAllWorkspaceUsersByIds(List<Long> workspaceUserIds);

    Optional<WorkspaceUser> findByUserWorkspaceId(Long userId, Long workspaceId);

    boolean existsByNicknameInWorkspace(Long workspaceId, String nickname);

    List<WorkspaceUser> findByWorkspaceIdAndTeamIsNull(Long workspaceId);

    List<WorkspaceUser> findWithWorkspaceByUserId(Long userId);

    List<WorkspaceUser> findByWorkspaceId(Long workspaceId);
}
