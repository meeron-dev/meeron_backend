package com.cmc.meeron.user.adapter.in;

import com.cmc.meeron.common.security.AuthUser;
import com.cmc.meeron.user.adapter.in.request.FindWorkspaceUserRequest;
import com.cmc.meeron.user.adapter.in.request.SetNameRequest;
import com.cmc.meeron.user.adapter.in.response.WorkspaceUserResponse;
import com.cmc.meeron.user.adapter.in.response.MyWorkspaceUsersResponse;
import com.cmc.meeron.user.adapter.in.response.WorkspaceUsersResponse;
import com.cmc.meeron.user.application.port.in.UserCommandUseCase;
import com.cmc.meeron.user.application.port.in.UserQueryUseCase;
import com.cmc.meeron.user.application.port.in.response.MeResponseDto;
import com.cmc.meeron.user.application.port.in.response.MyWorkspaceUserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserRestController {

    private final UserQueryUseCase userQueryUseCase;
    private final UserCommandUseCase userCommandUseCase;

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
    public WorkspaceUserResponse getMyWorkspaceUser(@PathVariable Long workspaceUserId) {
        MyWorkspaceUserResponseDto myWorkspaceUser = userQueryUseCase.getMyWorkspaceUser(workspaceUserId);
        return WorkspaceUserResponse.fromWorkspaceUser(myWorkspaceUser);
    }

    @GetMapping("/workspace-users")
    @ResponseStatus(HttpStatus.OK)
    public WorkspaceUsersResponse searchWorkspaceUsers(@Valid FindWorkspaceUserRequest findWorkspaceUserRequest) {
        List<MyWorkspaceUserResponseDto> myWorkspaceUserResponseDtos =
                userQueryUseCase.searchWorkspaceUsers(findWorkspaceUserRequest.toRequestDto());
        return WorkspaceUsersResponse.fromWorkspaceUsers(myWorkspaceUserResponseDtos);
    }

    @GetMapping("/teams/{teamId}/workspace-users")
    @ResponseStatus(HttpStatus.OK)
    public WorkspaceUsersResponse getTeamUsers(@PathVariable Long teamId) {
        List<MyWorkspaceUserResponseDto> myWorkspaceUserResponseDtos = userQueryUseCase.getTeamUsers(teamId);
        return WorkspaceUsersResponse.fromWorkspaceUsers(myWorkspaceUserResponseDtos);
    }

    @PatchMapping("/users/name")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void setName(@RequestBody @Valid SetNameRequest setNameRequest,
                        @AuthenticationPrincipal AuthUser authUser) {
        userCommandUseCase.setName(authUser, setNameRequest.getName());
    }
}
