package com.cmc.meeron.user.application.service;

import com.cmc.meeron.common.security.AuthUser;
import com.cmc.meeron.common.exception.user.WorkspaceUserNotFoundException;
import com.cmc.meeron.user.application.port.in.UserQueryUseCase;
import com.cmc.meeron.user.application.port.in.response.MeResponseDto;
import com.cmc.meeron.user.application.port.in.response.MyWorkspaceUserResponseDto;
import com.cmc.meeron.user.application.port.in.response.WorkspaceUserResponseDto;
import com.cmc.meeron.user.domain.WorkspaceUser;
import com.cmc.meeron.user.application.port.out.UserQueryPort;
import com.cmc.meeron.user.application.port.out.response.WorkspaceUserQueryResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
class UserQueryService implements UserQueryUseCase {

    private final UserQueryPort userQueryPort;

    @Override
    public MeResponseDto getMe(AuthUser authUser) {
        return MeResponseDto.fromUser(authUser.getUser());
    }

    @Override
    public List<MyWorkspaceUserResponseDto> getMyWorkspaceUsers(Long userId) {
        List<WorkspaceUser> myWorkspaceUsers = userQueryPort.findMyWorkspaceUsers(userId);
        return MyWorkspaceUserResponseDto.ofList(myWorkspaceUsers);
    }

    @Override
    public MyWorkspaceUserResponseDto getMyWorkspaceUser(Long workspaceUserId) {
        WorkspaceUser workspaceUser = userQueryPort.findWorkspaceUserById(workspaceUserId)
                .orElseThrow(WorkspaceUserNotFoundException::new);
        return MyWorkspaceUserResponseDto.of(workspaceUser);
    }

    @Override
    public List<WorkspaceUserResponseDto> searchWorkspaceUsers(Long workspaceId, String nickname) {
        List<WorkspaceUserQueryResponseDto> workspaceUserQueryResponseDtos = userQueryPort.findByWorkspaceIdNickname(workspaceId, nickname);
        return WorkspaceUserResponseDto.ofList(workspaceUserQueryResponseDtos);
    }

    @Override
    public List<WorkspaceUserResponseDto> getTeamUsers(Long teamId) {
        List<WorkspaceUserQueryResponseDto> workspaceUserQueryResponseDtos = userQueryPort.findByTeamId(teamId);
        return WorkspaceUserResponseDto.ofList(workspaceUserQueryResponseDtos);
    }
}
