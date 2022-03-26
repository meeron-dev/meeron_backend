package com.cmc.meeron.workspace.application.service;

import com.cmc.meeron.common.exception.user.NicknameDuplicateException;
import com.cmc.meeron.common.exception.user.WorkspaceUserNotFoundException;
import com.cmc.meeron.workspace.application.port.in.request.FindWorkspaceUserRequestDto;
import com.cmc.meeron.workspace.application.port.out.response.WorkspaceUserQueryResponseDto;
import com.cmc.meeron.workspace.application.port.in.response.MyWorkspaceUserResponseDto;
import com.cmc.meeron.workspace.application.port.in.WorkspaceUserQueryUseCase;
import com.cmc.meeron.workspace.application.port.out.WorkspaceUserQueryPort;
import com.cmc.meeron.workspace.domain.WorkspaceUser;
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
    public List<MyWorkspaceUserResponseDto> getMyWorkspaceUsers(Long userId) {
        List<WorkspaceUser> myWorkspaceUsers = workspaceUserQueryPort.findMyWorkspaceUsers(userId);
        return MyWorkspaceUserResponseDto.fromEntities(myWorkspaceUsers);
    }

    @Override
    public MyWorkspaceUserResponseDto getMyWorkspaceUser(Long workspaceUserId) {
        WorkspaceUser workspaceUser = workspaceUserQueryPort.findWorkspaceUserById(workspaceUserId)
                .orElseThrow(WorkspaceUserNotFoundException::new);
        return MyWorkspaceUserResponseDto.fromEntity(workspaceUser);
    }

    @Override
    public List<MyWorkspaceUserResponseDto> searchWorkspaceUsers(FindWorkspaceUserRequestDto findWorkspaceUserRequestDto) {
        List<WorkspaceUserQueryResponseDto> workspaceUserQueryResponseDtos =
                workspaceUserQueryPort.findByWorkspaceIdNickname(findWorkspaceUserRequestDto.getWorkspaceId(),
                        findWorkspaceUserRequestDto.getNickname());
        return MyWorkspaceUserResponseDto.fromQueryResponseDtos(workspaceUserQueryResponseDtos);
    }

    @Override
    public List<MyWorkspaceUserResponseDto> getTeamUsers(Long teamId) {
        List<WorkspaceUserQueryResponseDto> workspaceUserQueryResponseDtos = workspaceUserQueryPort.findByTeamId(teamId);
        return MyWorkspaceUserResponseDto.fromQueryResponseDtos(workspaceUserQueryResponseDtos);
    }

    @Override
    public void checkDuplicateNickname(FindWorkspaceUserRequestDto findWorkspaceUserRequestDto) {
        if (workspaceUserQueryPort.existsByNicknameInWorkspace(findWorkspaceUserRequestDto.getWorkspaceId(),
                findWorkspaceUserRequestDto.getNickname())) {
            throw new NicknameDuplicateException();
        }
    }
}
