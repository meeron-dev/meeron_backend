package com.cmc.meeron.user.adapter.in;

import com.cmc.meeron.common.security.AuthUser;
import com.cmc.meeron.user.adapter.in.request.SetNameRequest;
import com.cmc.meeron.user.adapter.in.response.UserNamedResponse;
import com.cmc.meeron.user.adapter.in.response.UserResponse;
import com.cmc.meeron.user.application.port.in.UserCommandUseCase;
import com.cmc.meeron.user.application.port.in.UserQueryUseCase;
import com.cmc.meeron.user.application.port.in.response.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserRestController {

    private final UserQueryUseCase userQueryUseCase;
    private final UserCommandUseCase userCommandUseCase;

    @GetMapping("/users/me")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse getMe(@AuthenticationPrincipal AuthUser authUser) {
        UserResponseDto userResponseDto = userQueryUseCase.getMe(authUser);
        return UserResponse.from(userResponseDto);
    }

    @GetMapping("/workspace-users/{workspaceUserId}/user")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse getUserFromWorkspaceUser(@PathVariable Long workspaceUserId) {
        UserResponseDto userResponseDto = userQueryUseCase.getUserByWorkspaceUserId(workspaceUserId);
        return UserResponse.from(userResponseDto);
    }

    @GetMapping("/users/name")
    @ResponseStatus(HttpStatus.OK)
    public UserNamedResponse checkNamedUser(@AuthenticationPrincipal AuthUser authUser) {
        boolean isNamed = userQueryUseCase.checkNamedUser(authUser.getUserId());
        return UserNamedResponse.of(isNamed);
    }

    @PatchMapping("/users/name")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void setName(@RequestBody @Valid SetNameRequest setNameRequest,
                        @AuthenticationPrincipal AuthUser authUser) {
        userCommandUseCase.setName(authUser, setNameRequest.getName());
    }

    @DeleteMapping("/users/quit")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void quit(@AuthenticationPrincipal AuthUser authUser) {
        userCommandUseCase.quit(authUser);
    }
}
