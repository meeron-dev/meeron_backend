package com.cmc.meeron.user.adapter.in;

import com.cmc.meeron.common.security.AuthUser;
import com.cmc.meeron.user.adapter.in.request.CreateWorkspaceUserRequest;
import com.cmc.meeron.user.adapter.in.request.FindWorkspaceUserRequest;
import com.cmc.meeron.user.adapter.in.request.SetNameRequest;
import com.cmc.meeron.user.adapter.in.response.*;
import com.cmc.meeron.user.application.port.in.UserCommandUseCase;
import com.cmc.meeron.user.application.port.in.UserQueryUseCase;
import com.cmc.meeron.user.application.port.in.response.MeResponseDto;
import com.cmc.meeron.user.application.port.in.response.MyWorkspaceUserResponseDto;
import com.cmc.meeron.user.application.port.in.response.WorkspaceUserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @GetMapping("/users/name")
    @ResponseStatus(HttpStatus.OK)
    public UserNamedResponse checkNamedUser(@AuthenticationPrincipal AuthUser authUser) {
        boolean isNamed = userQueryUseCase.checkNamedUser(authUser.getUserId());
        return UserNamedResponse.of(isNamed);
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

    @GetMapping("/workspace-users/nickname")
    @ResponseStatus(HttpStatus.OK)
    public CheckDuplicateNicknameResponse checkDuplicateNickname(@Valid FindWorkspaceUserRequest findWorkspaceUserRequest) {
        userQueryUseCase.checkDuplicateNickname(findWorkspaceUserRequest.toRequestDto());
        return CheckDuplicateNicknameResponse.isNotDuplicate();
    }

    @PostMapping(value = "/workspace-users/admin", consumes = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE
    })
    @ResponseStatus(HttpStatus.OK)
    public CreateWorkspaceUserResponse createWorkspaceUserAdmin(@RequestPart("request") @Valid CreateWorkspaceUserRequest createWorkspaceUserRequest,
                                                                @RequestPart(value = "files", required = false) MultipartFile file,
                                                                @AuthenticationPrincipal AuthUser authUser) {
        WorkspaceUserResponseDto workspaceUserResponseDto =
                userCommandUseCase.createWorkspaceUser(createWorkspaceUserRequest.toAdminRequestDto(file, authUser.getUserId()));
        return CreateWorkspaceUserResponse.fromDto(workspaceUserResponseDto);
    }

    @PostMapping(value = "/workspace-users", consumes = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE
    })
    @ResponseStatus(HttpStatus.OK)
    public CreateWorkspaceUserResponse createWorkspaceUser(@RequestPart("request") @Valid CreateWorkspaceUserRequest createWorkspaceUserRequest,
                                                           @RequestPart(value = "files", required = false) MultipartFile file,
                                                           @AuthenticationPrincipal AuthUser authUser) {
        WorkspaceUserResponseDto workspaceUserResponseDto =
                userCommandUseCase.createWorkspaceUser(createWorkspaceUserRequest.toRequestDto(file, authUser.getUserId()));
        return CreateWorkspaceUserResponse.fromDto(workspaceUserResponseDto);
    }
}
