package com.cmc.meeron.user.application.port.in;

import com.cmc.meeron.common.security.AuthUser;
import com.cmc.meeron.user.application.port.in.request.FindWorkspaceUserRequestDto;
import com.cmc.meeron.user.application.port.in.response.MeResponseDto;
import com.cmc.meeron.user.application.port.in.response.MyWorkspaceUserResponseDto;

import java.util.List;

public interface UserQueryUseCase {

    MeResponseDto getMe(AuthUser authUser);

    List<MyWorkspaceUserResponseDto> getMyWorkspaceUsers(Long userId);

    MyWorkspaceUserResponseDto getMyWorkspaceUser(Long workspaceUserId);

    List<MyWorkspaceUserResponseDto> searchWorkspaceUsers(FindWorkspaceUserRequestDto findWorkspaceUserRequestDto);

    List<MyWorkspaceUserResponseDto> getTeamUsers(Long teamId);
}
