package com.cmc.meeron.workspace.adapter.in;

import com.cmc.meeron.common.security.AuthUser;
import com.cmc.meeron.workspace.adapter.in.request.CreateWorkspaceUserRequest;
import com.cmc.meeron.workspace.adapter.in.request.FindWorkspaceUserRequest;
import com.cmc.meeron.workspace.application.port.in.response.MyWorkspaceUserResponseDto;
import com.cmc.meeron.workspace.application.port.in.response.WorkspaceUserResponseDto;
import com.cmc.meeron.workspace.adapter.in.response.*;
import com.cmc.meeron.workspace.application.port.in.WorkspaceUserCommandUseCase;
import com.cmc.meeron.workspace.application.port.in.WorkspaceUserQueryUseCase;
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
public class WorkspaceUserRestController {

    private final WorkspaceUserQueryUseCase workspaceUserQueryUseCase;
    private final WorkspaceUserCommandUseCase workspaceUserCommandUseCase;

    @GetMapping("/users/{userId}/workspace-users")
    @ResponseStatus(HttpStatus.OK)
    public MyWorkspaceUsersResponse getMyWorkspaceUsers(@PathVariable Long userId) {
        List<MyWorkspaceUserResponseDto> myWorkspaceUsers = workspaceUserQueryUseCase.getMyWorkspaceUsers(userId);
        return MyWorkspaceUsersResponse.fromWorkspaceUsers(myWorkspaceUsers);
    }

    @GetMapping("/workspace-users")
    @ResponseStatus(HttpStatus.OK)
    public WorkspaceUsersResponse searchWorkspaceUsers(@Valid FindWorkspaceUserRequest findWorkspaceUserRequest) {
        List<MyWorkspaceUserResponseDto> myWorkspaceUserResponseDtos =
                workspaceUserQueryUseCase.searchWorkspaceUsers(findWorkspaceUserRequest.toRequestDto());
        return WorkspaceUsersResponse.fromWorkspaceUsers(myWorkspaceUserResponseDtos);
    }

    @GetMapping("/workspace-users/{workspaceUserId}")
    @ResponseStatus(HttpStatus.OK)
    public WorkspaceUserResponse getMyWorkspaceUser(@PathVariable Long workspaceUserId) {
        MyWorkspaceUserResponseDto myWorkspaceUser = workspaceUserQueryUseCase.getMyWorkspaceUser(workspaceUserId);
        return WorkspaceUserResponse.fromWorkspaceUser(myWorkspaceUser);
    }

    @GetMapping("/teams/{teamId}/workspace-users")
    @ResponseStatus(HttpStatus.OK)
    public WorkspaceUsersResponse getTeamUsers(@PathVariable Long teamId) {
        List<MyWorkspaceUserResponseDto> myWorkspaceUserResponseDtos = workspaceUserQueryUseCase.getTeamUsers(teamId);
        return WorkspaceUsersResponse.fromWorkspaceUsers(myWorkspaceUserResponseDtos);
    }

    @GetMapping("/workspace-users/nickname")
    @ResponseStatus(HttpStatus.OK)
    public CheckDuplicateNicknameResponse checkDuplicateNickname(@Valid FindWorkspaceUserRequest findWorkspaceUserRequest) {
        workspaceUserQueryUseCase.checkDuplicateNickname(findWorkspaceUserRequest.toRequestDto());
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
                workspaceUserCommandUseCase.createWorkspaceUser(createWorkspaceUserRequest.toAdminRequestDto(file, authUser.getUserId()));
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
                workspaceUserCommandUseCase.createWorkspaceUser(createWorkspaceUserRequest.toRequestDto(file, authUser.getUserId()));
        return CreateWorkspaceUserResponse.fromDto(workspaceUserResponseDto);
    }
}
