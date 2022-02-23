package com.cmc.meeron.user.application;

import com.cmc.meeron.auth.domain.AuthUser;
import com.cmc.meeron.user.application.dto.response.MeResponseDto;
import com.cmc.meeron.user.application.dto.response.MyWorkspaceUserResponseDto;

import java.util.List;

public interface UserQueryUseCase {

    MeResponseDto getMe(AuthUser authUser);

    List<MyWorkspaceUserResponseDto> getMyWorkspaceUsers(Long userId);

    MyWorkspaceUserResponseDto getMyWorkspaceUser(Long workspaceUserId);

    // TODO: 2022/02/23 kobeomseok95 회원의 모든 워크스페이스, 회원의 지정한 하나의 워크스페이스 조회
    // TODO: 2022/02/23 kobeomseok95 /api/users/id/workspaces, /api/users/id/workspaces/id
}
