package com.cmc.meeron.user.domain;

import com.cmc.meeron.user.domain.dto.WorkspaceUserQueryResponseDto;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    Optional<User> findByEmail(String email);

    User save(User user);

    List<WorkspaceUser> findMyWorkspaceUsers(Long userId);

    Optional<WorkspaceUser> findWorkspaceUserById(Long workspaceUserId);

    List<WorkspaceUserQueryResponseDto> findByWorkspaceIdNickname(Long workspaceId, String nickname);

    List<WorkspaceUserQueryResponseDto> findByTeamId(Long teamId);
}
