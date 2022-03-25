package com.cmc.meeron.user.application.service;

import com.cmc.meeron.common.exception.user.NicknameDuplicateException;
import com.cmc.meeron.common.exception.user.UserNotFoundException;
import com.cmc.meeron.common.exception.user.WorkspaceUserNotFoundException;
import com.cmc.meeron.common.security.AuthUser;
import com.cmc.meeron.user.application.port.in.UserQueryUseCase;
import com.cmc.meeron.user.application.port.in.request.FindWorkspaceUserRequestDto;
import com.cmc.meeron.user.application.port.in.response.MeResponseDto;
import com.cmc.meeron.user.application.port.in.response.MyWorkspaceUserResponseDto;
import com.cmc.meeron.user.application.port.out.UserQueryPort;
import com.cmc.meeron.user.application.port.out.response.WorkspaceUserQueryResponseDto;
import com.cmc.meeron.user.domain.User;
import com.cmc.meeron.user.domain.WorkspaceUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class UserQueryService implements UserQueryUseCase {

    private final UserQueryPort userQueryPort;

    @Override
    public MeResponseDto getMe(AuthUser authUser) {
        return MeResponseDto.fromUser(authUser.getUser());
    }

    @Override
    public List<MyWorkspaceUserResponseDto> getMyWorkspaceUsers(Long userId) {
        List<WorkspaceUser> myWorkspaceUsers = userQueryPort.findMyWorkspaceUsers(userId);
        return MyWorkspaceUserResponseDto.fromEntities(myWorkspaceUsers);
    }

    @Override
    public MyWorkspaceUserResponseDto getMyWorkspaceUser(Long workspaceUserId) {
        WorkspaceUser workspaceUser = userQueryPort.findWorkspaceUserById(workspaceUserId)
                .orElseThrow(WorkspaceUserNotFoundException::new);
        return MyWorkspaceUserResponseDto.fromEntity(workspaceUser);
    }

    @Override
    public List<MyWorkspaceUserResponseDto> searchWorkspaceUsers(FindWorkspaceUserRequestDto findWorkspaceUserRequestDto) {
        List<WorkspaceUserQueryResponseDto> workspaceUserQueryResponseDtos =
                userQueryPort.findByWorkspaceIdNickname(findWorkspaceUserRequestDto.getWorkspaceId(),
                        findWorkspaceUserRequestDto.getNickname());
        return MyWorkspaceUserResponseDto.fromQueryResponseDtos(workspaceUserQueryResponseDtos);
    }

    @Override
    public List<MyWorkspaceUserResponseDto> getTeamUsers(Long teamId) {
        List<WorkspaceUserQueryResponseDto> workspaceUserQueryResponseDtos = userQueryPort.findByTeamId(teamId);
        return MyWorkspaceUserResponseDto.fromQueryResponseDtos(workspaceUserQueryResponseDtos);
    }

    @Override
    public boolean checkNamedUser(Long userId) {
        User user = userQueryPort.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        return StringUtils.hasText(user.getName());
    }

    @Override
    public void checkDuplicateNickname(FindWorkspaceUserRequestDto findWorkspaceUserRequestDto) {
        if (userQueryPort.existsByNicknameInWorkspace(findWorkspaceUserRequestDto.getWorkspaceId(),
                findWorkspaceUserRequestDto.getNickname())) {
            throw new NicknameDuplicateException();
        }
    }
}
