package com.cmc.meeron.user.adapter.in;

import com.cmc.meeron.common.security.AuthUser;
import com.cmc.meeron.user.adapter.in.request.FindWorkspaceUsersParameters;
import com.cmc.meeron.user.adapter.in.response.MyWorkspaceUserResponse;
import com.cmc.meeron.user.adapter.in.response.MyWorkspaceUsersResponse;
import com.cmc.meeron.user.adapter.in.response.WorkspaceUserResponse;
import com.cmc.meeron.user.application.port.in.UserQueryUseCase;
import com.cmc.meeron.user.application.port.in.response.MeResponseDto;
import com.cmc.meeron.user.application.port.in.response.MyWorkspaceUserResponseDto;
import com.cmc.meeron.user.application.port.in.response.WorkspaceUserResponseDto;
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

    @GetMapping("/workspace-users")
    @ResponseStatus(HttpStatus.OK)
    public WorkspaceUserResponse searchWorkspaceUsers(@Valid FindWorkspaceUsersParameters findWorkspaceUsersParameters) {
        List<WorkspaceUserResponseDto> workspaceUserResponseDtos =
                userQueryUseCase.searchWorkspaceUsers(findWorkspaceUsersParameters.getWorkspaceId(), findWorkspaceUsersParameters.getNickname());
        return WorkspaceUserResponse.fromList(workspaceUserResponseDtos);
    }

    @GetMapping("/workspace-users/{workspaceUserId}")
    @ResponseStatus(HttpStatus.OK)
    public MyWorkspaceUserResponse getMyWorkspaceUser(@PathVariable Long workspaceUserId) {
        MyWorkspaceUserResponseDto myWorkspaceUser = userQueryUseCase.getMyWorkspaceUser(workspaceUserId);
        return MyWorkspaceUserResponse.fromWorkspaceUser(myWorkspaceUser);
    }

    @GetMapping("/teams/{teamId}/workspace-users")
    @ResponseStatus(HttpStatus.OK)
    public WorkspaceUserResponse searchWorkspaceUsers(@PathVariable Long teamId) {
        List<WorkspaceUserResponseDto> workspaceUserResponseDtos = userQueryUseCase.getTeamUsers(teamId);
        return WorkspaceUserResponse.fromList(workspaceUserResponseDtos);
    }
}
