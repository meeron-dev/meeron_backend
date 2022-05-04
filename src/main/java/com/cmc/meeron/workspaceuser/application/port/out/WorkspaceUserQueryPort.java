package com.cmc.meeron.workspaceuser.application.port.out;

import com.cmc.meeron.workspaceuser.application.port.out.response.WorkspaceUserQuerydslResponseDto;
import com.cmc.meeron.workspaceuser.domain.WorkspaceUser;

import java.util.List;
import java.util.Optional;

public interface WorkspaceUserQueryPort {

    List<WorkspaceUser> findMyWorkspaceUsers(Long userId);

    Optional<WorkspaceUser> findById(Long workspaceUserId);

    List<WorkspaceUserQuerydslResponseDto> findByWorkspaceIdNickname(Long workspaceId, String nickname);

    List<WorkspaceUserQuerydslResponseDto> findQueryByTeamId(Long teamId);

    List<WorkspaceUser> findByTeamId(Long teamId);

    List<WorkspaceUser> findAllWorkspaceUsersByIds(List<Long> workspaceUserIds);

    boolean existsByNicknameInWorkspace(Long workspaceId, String nickname);

    List<WorkspaceUser> findByWorkspaceIdAndTeamIsNull(Long workspaceId);

    List<WorkspaceUser> findByWorkspaceId(Long workspaceId);
}
