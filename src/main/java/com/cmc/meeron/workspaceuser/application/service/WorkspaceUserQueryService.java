package com.cmc.meeron.workspaceuser.application.service;

import com.cmc.meeron.common.exception.workspace.NicknameDuplicateException;
import com.cmc.meeron.common.exception.workspace.WorkspaceUserNotFoundException;
import com.cmc.meeron.workspaceuser.application.port.in.WorkspaceUserQueryUseCase;
import com.cmc.meeron.workspaceuser.application.port.in.request.FindWorkspaceUserRequestDto;
import com.cmc.meeron.workspaceuser.application.port.in.response.WorkspaceUserResponseDto;
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
    public List<WorkspaceUserResponseDto> getMyWorkspaceUsers(Long userId) {
        List<WorkspaceUser> myWorkspaceUsers = workspaceUserQueryPort.findMyWorkspaceUsers(userId);
        return WorkspaceUserResponseDto.fromEntities(myWorkspaceUsers);
    }

    @Override
    public WorkspaceUserResponseDto getMyWorkspaceUser(Long workspaceUserId) {
        WorkspaceUser workspaceUser = workspaceUserQueryPort.findById(workspaceUserId)
                .orElseThrow(WorkspaceUserNotFoundException::new);
        return WorkspaceUserResponseDto.from(workspaceUser);
    }

    @Override
    public List<WorkspaceUserResponseDto> searchWorkspaceUsers(FindWorkspaceUserRequestDto findWorkspaceUserRequestDto) {
        List<WorkspaceUserQuerydslResponseDto> workspaceUserQuerydslResponseDtos =
                workspaceUserQueryPort.findByWorkspaceIdNickname(findWorkspaceUserRequestDto.getWorkspaceId(),
                        findWorkspaceUserRequestDto.getNickname());
        return WorkspaceUserResponseDto.from(workspaceUserQuerydslResponseDtos);
    }

    @Override
    public List<WorkspaceUserResponseDto> getTeamUsers(Long teamId) {
        List<WorkspaceUserQuerydslResponseDto> workspaceUserQuerydslResponseDtos = workspaceUserQueryPort.findQueryByTeamId(teamId);
        return WorkspaceUserResponseDto.from(workspaceUserQuerydslResponseDtos);
    }

    @Override
    public void checkDuplicateNickname(FindWorkspaceUserRequestDto findWorkspaceUserRequestDto) {
        if (workspaceUserQueryPort.existsByNicknameInWorkspace(findWorkspaceUserRequestDto.getWorkspaceId(),
                findWorkspaceUserRequestDto.getNickname())) {
            throw new NicknameDuplicateException();
        }
    }

    @Override
    public List<WorkspaceUserResponseDto> getNoneTeamWorkspaceUsers(Long workspaceId) {
        List<WorkspaceUser> workspaceUsers = workspaceUserQueryPort.findByWorkspaceIdAndTeamIsNull(workspaceId);
        return WorkspaceUserResponseDto.fromEntities(workspaceUsers);
    }
}
