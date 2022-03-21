package com.cmc.meeron.user.application.service;

import com.cmc.meeron.common.exception.user.NicknameDuplicateException;
import com.cmc.meeron.common.exception.user.UserNotFoundException;
import com.cmc.meeron.common.exception.workspace.WorkspaceNotFoundException;
import com.cmc.meeron.common.security.AuthUser;
import com.cmc.meeron.file.application.port.in.FileManager;
import com.cmc.meeron.user.application.port.in.UserCommandUseCase;
import com.cmc.meeron.user.application.port.in.request.CreateWorkspaceUserRequestDto;
import com.cmc.meeron.user.application.port.in.response.WorkspaceUserResponseDto;
import com.cmc.meeron.user.application.port.out.UserCommandPort;
import com.cmc.meeron.user.application.port.out.UserQueryPort;
import com.cmc.meeron.user.domain.User;
import com.cmc.meeron.user.domain.WorkspaceUser;
import com.cmc.meeron.user.domain.WorkspaceUserInfo;
import com.cmc.meeron.workspace.application.port.out.WorkspaceQueryPort;
import com.cmc.meeron.workspace.domain.Workspace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional
class UserCommandService implements UserCommandUseCase {

    private final UserQueryPort userQueryPort;
    private final WorkspaceQueryPort workspaceQueryPort;
    private final UserCommandPort userCommandPort;
    private final FileManager fileManager;

    @Override
    public void setName(AuthUser authUser, String name) {
        User user = userQueryPort.findById(authUser.getUserId())
                .orElseThrow(UserNotFoundException::new);
        user.setName(name);
    }

    @Override
    public WorkspaceUserResponseDto createWorkspaceUser(CreateWorkspaceUserRequestDto createWorkspaceUserRequestDto) {
        if (userQueryPort.existsByNicknameInWorkspace(createWorkspaceUserRequestDto.getWorkspaceId(), createWorkspaceUserRequestDto.getNickname())) {
            throw new NicknameDuplicateException();
        }
        User user = userQueryPort.findById(createWorkspaceUserRequestDto.getUserId())
                .orElseThrow(UserNotFoundException::new);
        Workspace workspace = workspaceQueryPort.findById(createWorkspaceUserRequestDto.getWorkspaceId())
                .orElseThrow(WorkspaceNotFoundException::new);
        String savedImageUrl = checkNullAndSaveImage(createWorkspaceUserRequestDto);
        WorkspaceUserInfo workspaceUserInfo = createWorkspaceUserRequestDto.toWorkspaceUserInfo(savedImageUrl);
        WorkspaceUser workspaceUser = userCommandPort.saveWorkspaceUser(WorkspaceUser.of(user, workspace, workspaceUserInfo));
        return WorkspaceUserResponseDto.of(workspaceUser);
    }

    private String checkNullAndSaveImage(CreateWorkspaceUserRequestDto createWorkspaceUserRequestDto) {
        if (createWorkspaceUserRequestDto.getProfileImage() != null &&
                StringUtils.hasText(createWorkspaceUserRequestDto.getOriginalFilename())) {
            return fileManager.saveProfileImage(createWorkspaceUserRequestDto.getProfileImage());
        }
        return "";
    }
}
