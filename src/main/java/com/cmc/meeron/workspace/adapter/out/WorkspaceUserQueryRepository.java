package com.cmc.meeron.workspace.adapter.out;

import com.cmc.meeron.workspace.application.port.out.WorkspaceUserQueryPort;
import com.cmc.meeron.workspace.application.port.out.response.WorkspaceUserQuerydslResponseDto;
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
    public Optional<WorkspaceUser> findById(Long workspaceUserId) {
        return workspaceUserJpaRepository.findById(workspaceUserId);
    }

    @Override
    public List<WorkspaceUserQuerydslResponseDto> findByWorkspaceIdNickname(Long workspaceId, String nickname) {
        return workspaceUserQuerydslRepository.findByWorkspaceIdNickname(workspaceId, nickname);
    }

    @Override
    public List<WorkspaceUserQuerydslResponseDto> findQueryByTeamId(Long teamId) {
        return workspaceUserQuerydslRepository.findByTeamId(teamId);
    }

    @Override
    public List<WorkspaceUser> findByTeamId(Long teamId) {
        return workspaceUserJpaRepository.findByTeamId(teamId);
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

    @Override
    public List<WorkspaceUser> findByWorkspaceIdAndTeamIsNull(Long workspaceId) {
        return workspaceUserJpaRepository.findByWorkspaceIdAndTeamIsNull(workspaceId);
    }

    @Override
    public List<WorkspaceUser> findWithWorkspaceByUserId(Long userId) {
        return workspaceUserJpaRepository.findWithWorkspaceByUserId(userId);
    }

    @Override
    public List<WorkspaceUser> findByWorkspaceId(Long workspaceId) {
        return workspaceUserJpaRepository.findByWorkspaceId(workspaceId);
    }
}
