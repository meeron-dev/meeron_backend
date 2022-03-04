package com.cmc.meeron.user.application.port.in;

import com.cmc.meeron.common.security.AuthUser;
import com.cmc.meeron.user.application.port.in.response.MeResponseDto;
import com.cmc.meeron.user.application.port.in.response.MyWorkspaceUserResponseDto;
import com.cmc.meeron.user.application.port.in.response.WorkspaceUserResponseDto;

import java.util.List;

public interface UserQueryUseCase {

    MeResponseDto getMe(AuthUser authUser);

    List<MyWorkspaceUserResponseDto> getMyWorkspaceUsers(Long userId);

    MyWorkspaceUserResponseDto getMyWorkspaceUser(Long workspaceUserId);

    List<WorkspaceUserResponseDto> searchWorkspaceUsers(Long workspaceId, String nickname);

    List<WorkspaceUserResponseDto> getTeamUsers(Long teamId);
}
