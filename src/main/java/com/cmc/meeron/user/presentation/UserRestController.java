package com.cmc.meeron.user.presentation;

import com.cmc.meeron.auth.domain.AuthUser;
import com.cmc.meeron.user.application.UserQueryUseCase;
import com.cmc.meeron.user.application.dto.response.MeResponseDto;
import com.cmc.meeron.user.application.dto.response.MyWorkspaceUserResponseDto;
import com.cmc.meeron.user.presentation.dto.response.MyWorkspaceUserResponse;
import com.cmc.meeron.user.presentation.dto.response.MyWorkspaceUsersResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserRestController {

    private final UserQueryUseCase userQueryUseCase;

    @GetMapping("/users/me")
    @ResponseStatus(HttpStatus.OK)
    public MeResponseDto getUser(@AuthenticationPrincipal AuthUser authUser) {
        return userQueryUseCase.getMe(authUser);
    }

    @GetMapping("/users/{userId}/workspace-users")
    @ResponseStatus(HttpStatus.OK)
    public MyWorkspaceUsersResponse getMyWorkspaceUsers(@PathVariable Long userId) {
        List<MyWorkspaceUserResponseDto> myWorkspaceUsers = userQueryUseCase.getMyWorkspaceUsers(userId);
        return MyWorkspaceUsersResponse.fromWorkspaceUsers(myWorkspaceUsers);
    }

    @GetMapping("/workspace-users/{workspaceUserId}")
    @ResponseStatus(HttpStatus.OK)
    public MyWorkspaceUserResponse getMyWorkspaceUser(@PathVariable Long workspaceUserId) {
        MyWorkspaceUserResponseDto myWorkspaceUser = userQueryUseCase.getMyWorkspaceUser(workspaceUserId);
        return MyWorkspaceUserResponse.fromWorkspaceUser(myWorkspaceUser);
    }
}
