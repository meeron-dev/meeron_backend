package com.cmc.meeron.user.application;

import com.cmc.meeron.auth.domain.AuthUser;
import com.cmc.meeron.common.exception.user.WorkspaceUserNotFoundException;
import com.cmc.meeron.user.application.dto.response.MeResponseDto;
import com.cmc.meeron.user.application.dto.response.MyWorkspaceUserResponseDto;
import com.cmc.meeron.user.domain.WorkspaceUser;
import com.cmc.meeron.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
class UserService implements UserQueryUseCase {

    private final UserRepository userRepository;

    @Override
    public MeResponseDto getMe(AuthUser authUser) {
        return MeResponseDto.fromUser(authUser.getUser());
    }

    @Override
    public List<MyWorkspaceUserResponseDto> getMyWorkspaceUsers(Long userId) {
        List<WorkspaceUser> myWorkspaceUsers = userRepository.findMyWorkspaceUsers(userId);
        return UserApplicationAssembler.fromWorkspaceUsers(myWorkspaceUsers);
    }

    @Override
    public MyWorkspaceUserResponseDto getMyWorkspaceUser(Long workspaceUserId) {
        WorkspaceUser workspaceUser = userRepository.findWorkspaceUserById(workspaceUserId)
                .orElseThrow(WorkspaceUserNotFoundException::new);
        return UserApplicationAssembler.fromWorkspaceUser(workspaceUser);
    }
}
