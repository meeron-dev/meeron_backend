package com.cmc.meeron.workspaceuser.application.service;

import com.cmc.meeron.common.exception.workspace.NicknameDuplicateException;
import com.cmc.meeron.common.exception.workspace.WorkspaceUserNotFoundException;
import com.cmc.meeron.user.domain.User;
import com.cmc.meeron.workspaceuser.application.port.in.request.FindWorkspaceUserRequestDto;
import com.cmc.meeron.workspaceuser.application.port.in.response.UserResponseDto;
import com.cmc.meeron.workspaceuser.application.port.in.response.WorkspaceUserQueryResponseDto;
import com.cmc.meeron.workspaceuser.application.port.in.WorkspaceUserQueryUseCase;
import com.cmc.meeron.workspaceuser.application.port.out.WorkspaceUserQueryPort;
import com.cmc.meeron.workspaceuser.application.port.out.response.WorkspaceUserQuerydslResponseDto;
import com.cmc.meeron.workspaceuser.domain.WorkspaceUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class WorkspaceUserQueryService implements WorkspaceUserQueryUseCase {

    private final WorkspaceUserQueryPort workspaceUserQueryPort;

    @Override
    public List<WorkspaceUserQueryResponseDto> getMyWorkspaceUsers(Long userId) {
        List<WorkspaceUser> myWorkspaceUsers = workspaceUserQueryPort.findMyWorkspaceUsers(userId);
        return WorkspaceUserQueryResponseDto.fromEntities(myWorkspaceUsers);
    }

    @Override
    public WorkspaceUserQueryResponseDto getMyWorkspaceUser(Long workspaceUserId) {
        WorkspaceUser workspaceUser = workspaceUserQueryPort.findById(workspaceUserId)
                .orElseThrow(WorkspaceUserNotFoundException::new);
        return WorkspaceUserQueryResponseDto.fromEntity(workspaceUser);
    }

    @Override
    public List<WorkspaceUserQueryResponseDto> searchWorkspaceUsers(FindWorkspaceUserRequestDto findWorkspaceUserRequestDto) {
        List<WorkspaceUserQuerydslResponseDto> workspaceUserQuerydslResponseDtos =
                workspaceUserQueryPort.findByWorkspaceIdNickname(findWorkspaceUserRequestDto.getWorkspaceId(),
                        findWorkspaceUserRequestDto.getNickname());
        return WorkspaceUserQueryResponseDto.fromQueryResponseDtos(workspaceUserQuerydslResponseDtos);
    }

    @Override
    public List<WorkspaceUserQueryResponseDto> getTeamUsers(Long teamId) {
        List<WorkspaceUserQuerydslResponseDto> workspaceUserQuerydslResponseDtos = workspaceUserQueryPort.findQueryByTeamId(teamId);
        return WorkspaceUserQueryResponseDto.fromQueryResponseDtos(workspaceUserQuerydslResponseDtos);
    }

    @Override
    public void checkDuplicateNickname(FindWorkspaceUserRequestDto findWorkspaceUserRequestDto) {
        if (workspaceUserQueryPort.existsByNicknameInWorkspace(findWorkspaceUserRequestDto.getWorkspaceId(),
                findWorkspaceUserRequestDto.getNickname())) {
            throw new NicknameDuplicateException();
        }
    }

    @Override
    public List<WorkspaceUserQueryResponseDto> getNoneTeamWorkspaceUsers(Long workspaceId) {
        List<WorkspaceUser> workspaceUsers = workspaceUserQueryPort.findByWorkspaceIdAndTeamIsNull(workspaceId);
        return WorkspaceUserQueryResponseDto.fromEntities(workspaceUsers);
    }

    @Override
    public UserResponseDto getUser(Long workspaceUserId) {
        User user = workspaceUserQueryPort.findWithUserById(workspaceUserId)
                .orElseThrow(WorkspaceUserNotFoundException::new)
                .getUser();
        return UserResponseDto.fromEntity(user);
    }
}
