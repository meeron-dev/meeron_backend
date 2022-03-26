package com.cmc.meeron.workspace.adapter.out;

import com.cmc.meeron.workspace.application.port.out.WorkspaceUserQueryPort;
import com.cmc.meeron.workspace.application.port.out.response.WorkspaceUserQueryResponseDto;
import com.cmc.meeron.workspace.domain.WorkspaceUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
class WorkspaceUserQueryRepository implements WorkspaceUserQueryPort {

    private final WorkspaceUserQuerydslRepository workspaceUserQuerydslRepository;
    private final WorkspaceUserJpaRepository workspaceUserJpaRepository;

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

    @Override
    public List<WorkspaceUser> findAllWorkspaceUsersByIds(List<Long> workspaceUserIds) {
        return workspaceUserJpaRepository.findAllById(workspaceUserIds);
    }

    @Override
    public Optional<WorkspaceUser> findByUserWorkspaceId(Long userId, Long workspaceId) {
        return workspaceUserJpaRepository.findByUserWorkspaceId(userId, workspaceId);
    }

    @Override
    public boolean existsByNicknameInWorkspace(Long workspaceId, String nickname) {
        return workspaceUserQuerydslRepository.existsByNicknameInWorkspace(workspaceId, nickname);
    }
}
