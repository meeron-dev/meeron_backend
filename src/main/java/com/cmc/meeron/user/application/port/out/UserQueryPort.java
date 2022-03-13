package com.cmc.meeron.user.application.port.out;

import com.cmc.meeron.user.domain.User;
import com.cmc.meeron.user.domain.WorkspaceUser;
import com.cmc.meeron.user.application.port.out.response.WorkspaceUserQueryResponseDto;

import java.util.List;
import java.util.Optional;

public interface UserQueryPort {

    Optional<User> findByEmail(String email);

    List<WorkspaceUser> findMyWorkspaceUsers(Long userId);

    Optional<WorkspaceUser> findWorkspaceUserById(Long workspaceUserId);

    List<WorkspaceUserQueryResponseDto> findByWorkspaceIdNickname(Long workspaceId, String nickname);

    List<WorkspaceUserQueryResponseDto> findByTeamId(Long teamId);

    List<WorkspaceUser> findAllWorkspaceUsersByIds(List<Long> workspaceUserIds);

    Optional<WorkspaceUser> findByUserWorkspaceId(Long userId, Long workspaceId);
}
