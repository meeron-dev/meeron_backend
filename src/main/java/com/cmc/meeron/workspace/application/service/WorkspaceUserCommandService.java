package com.cmc.meeron.workspace.application.service;

import com.cmc.meeron.common.advice.workspaceuser.CheckWorkspaceAdmin;
import com.cmc.meeron.common.exception.team.TeamNotFoundException;
import com.cmc.meeron.common.exception.workspace.NicknameDuplicateException;
import com.cmc.meeron.common.exception.user.UserNotFoundException;
import com.cmc.meeron.common.exception.workspace.NotAllFoundWorkspaceUsersException;
import com.cmc.meeron.common.exception.workspace.WorkspaceUserNotFoundException;
import com.cmc.meeron.common.exception.workspace.WorkspaceNotFoundException;
import com.cmc.meeron.file.application.port.in.FileManager;
import com.cmc.meeron.team.application.port.out.TeamQueryPort;
import com.cmc.meeron.workspace.application.port.in.request.KickOutTeamUserRequestDto;
import com.cmc.meeron.workspace.application.port.in.request.JoinTeamUsersRequestDto;
import com.cmc.meeron.team.domain.Team;
import com.cmc.meeron.user.application.port.out.UserQueryPort;
import com.cmc.meeron.user.domain.User;
import com.cmc.meeron.workspace.application.port.in.request.CreateWorkspaceUserRequestDto;
import com.cmc.meeron.workspace.application.port.in.request.ModifyWorkspaceUserRequestDto;
import com.cmc.meeron.workspace.application.port.in.response.WorkspaceUserCommandResponseDto;
import com.cmc.meeron.workspace.application.port.in.WorkspaceUserCommandUseCase;
import com.cmc.meeron.workspace.application.port.out.WorkspaceQueryPort;
import com.cmc.meeron.workspace.application.port.out.WorkspaceUserCommandPort;
import com.cmc.meeron.workspace.application.port.out.WorkspaceUserQueryPort;
import com.cmc.meeron.workspace.domain.Workspace;
import com.cmc.meeron.workspace.domain.WorkspaceUser;
import com.cmc.meeron.workspace.domain.WorkspaceUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
class WorkspaceUserCommandService implements WorkspaceUserCommandUseCase {

    private final UserQueryPort userQueryPort;
    private final WorkspaceQueryPort workspaceQueryPort;
    private final TeamQueryPort teamQueryPort;
    private final FileManager fileManager;
    private final WorkspaceUserQueryPort workspaceUserQueryPort;
    private final WorkspaceUserCommandPort workspaceUserCommandPort;

    @Override
    public WorkspaceUserCommandResponseDto createWorkspaceUser(CreateWorkspaceUserRequestDto createWorkspaceUserRequestDto) {
        checkDuplicateNickname(createWorkspaceUserRequestDto.getWorkspaceId(),
                createWorkspaceUserRequestDto.getNickname());
        User user = userQueryPort.findById(createWorkspaceUserRequestDto.getUserId())
                .orElseThrow(UserNotFoundException::new);
        Workspace workspace = workspaceQueryPort.findById(createWorkspaceUserRequestDto.getWorkspaceId())
                .orElseThrow(WorkspaceNotFoundException::new);
        String savedImageUrl = checkNullAndSaveImage(createWorkspaceUserRequestDto.getProfileImage());
        WorkspaceUserInfo workspaceUserInfo = createWorkspaceUserRequestDto.toWorkspaceUserInfo(savedImageUrl);
        WorkspaceUser workspaceUser = workspaceUserCommandPort.saveWorkspaceUser(WorkspaceUser.of(user, workspace, workspaceUserInfo));
        return WorkspaceUserCommandResponseDto.fromEntity(workspaceUser);
    }

    private void checkDuplicateNickname(Long workspaceUserId, String nickname) {
        if (workspaceUserQueryPort.existsByNicknameInWorkspace(workspaceUserId, nickname)) {
            throw new NicknameDuplicateException();
        }
    }

    private String checkNullAndSaveImage(MultipartFile imageFile) {
        if (imageFile != null &&
                StringUtils.hasText(imageFile.getOriginalFilename())) {
            return fileManager.saveProfileImage(imageFile);
        }
        return "";
    }

    @Override
    public WorkspaceUserCommandResponseDto modifyWorkspaceUser(ModifyWorkspaceUserRequestDto modifyWorkspaceUserRequestDto) {
        WorkspaceUser workspaceUser = workspaceUserQueryPort.findById(modifyWorkspaceUserRequestDto.getWorkspaceUserId())
                .orElseThrow(WorkspaceUserNotFoundException::new);
        checkDuplicateNickname(workspaceUser.getWorkspace().getId(), modifyWorkspaceUserRequestDto.getNickname());
        String savedImageUrl = checkNullAndSaveImage(modifyWorkspaceUserRequestDto.getProfileImage());
        WorkspaceUserInfo workspaceUserInfo = modifyWorkspaceUserRequestDto.toWorkspaceUserInfo(savedImageUrl);
        workspaceUser.modifyInfo(workspaceUserInfo);
        return WorkspaceUserCommandResponseDto.fromEntity(workspaceUser);
    }

    @Override
    @CheckWorkspaceAdmin
    public void joinTeamUsers(JoinTeamUsersRequestDto joinTeamUsersRequestDto) {
        Team team = teamQueryPort.findById(joinTeamUsersRequestDto.getTeamId())
                .orElseThrow(TeamNotFoundException::new);
        List<WorkspaceUser> workspaceUsers = workspaceUserQueryPort.findAllWorkspaceUsersByIds(joinTeamUsersRequestDto.getWorkspaceUserIds());
        validCountWorkspaceUsers(joinTeamUsersRequestDto.getWorkspaceUserIds(), workspaceUsers);
        workspaceUsers.forEach(workspaceUser -> workspaceUser.joinTeam(team));
    }

    private void validCountWorkspaceUsers(List<Long> workspaceUserIds, List<WorkspaceUser> workspaceUsers) {
        if (workspaceUsers.size() != workspaceUserIds.size()) {
            throw new NotAllFoundWorkspaceUsersException();
        }
    }

    @Override
    @CheckWorkspaceAdmin
    public void kickOutTeamUser(KickOutTeamUserRequestDto kickOutTeamUserRequestDto) {
        WorkspaceUser exitWorkspaceUser = workspaceUserQueryPort.findById(kickOutTeamUserRequestDto.getKickOutWorkspaceUserId())
                .orElseThrow(WorkspaceUserNotFoundException::new);
        exitWorkspaceUser.exitTeam();
    }
}
