package com.cmc.meeron.user.application.port.in;

import com.cmc.meeron.common.security.AuthUser;
import com.cmc.meeron.user.application.port.in.request.CreateWorkspaceUserRequestDto;
import com.cmc.meeron.user.application.port.in.response.WorkspaceUserResponseDto;

public interface UserCommandUseCase {

    void setName(AuthUser authUser, String name);

    WorkspaceUserResponseDto createWorkspaceUser(CreateWorkspaceUserRequestDto createWorkspaceUserRequestDto);
}
