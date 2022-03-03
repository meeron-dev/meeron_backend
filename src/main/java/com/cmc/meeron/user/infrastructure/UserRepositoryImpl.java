package com.cmc.meeron.user.infrastructure;

import com.cmc.meeron.user.domain.User;
import com.cmc.meeron.user.domain.UserRepository;
import com.cmc.meeron.user.domain.WorkspaceUser;
import com.cmc.meeron.user.domain.dto.WorkspaceUserQueryResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;
    private final WorkspaceUserJpaRepository workspaceUserJpaRepository;
    private final WorkspaceUserQuerydslRepository workspaceUserQuerydslRepository;

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email);
    }

    @Override
    public User save(User user) {
        return userJpaRepository.save(user);
    }

    @Override
    public List<WorkspaceUser> findMyWorkspaceUsers(Long userId) {
        return workspaceUserJpaRepository.findByUserId(userId);
    }

    @Override
    public Optional<WorkspaceUser> findWorkspaceUserById(Long workspaceUserId) {
        return workspaceUserJpaRepository.findById(workspaceUserId);
    }

    @Override
    public List<WorkspaceUserQueryResponseDto> findByWorkspaceIdNickname(Long workspaceId, String nickname) {
        return workspaceUserQuerydslRepository.findByWorkspaceIdNickname(workspaceId, nickname);
    }

    @Override
    public List<WorkspaceUserQueryResponseDto> findByTeamId(Long teamId) {
        return workspaceUserQuerydslRepository.findByTeamId(teamId);
    }
}
