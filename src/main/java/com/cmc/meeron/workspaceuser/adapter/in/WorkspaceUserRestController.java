package com.cmc.meeron.workspaceuser.adapter.in;

import com.cmc.meeron.common.security.AuthUser;
import com.cmc.meeron.workspaceuser.adapter.in.request.*;
import com.cmc.meeron.workspaceuser.adapter.in.response.*;
import com.cmc.meeron.workspaceuser.application.port.in.WorkspaceUserCommandUseCase;
import com.cmc.meeron.workspaceuser.application.port.in.WorkspaceUserQueryUseCase;
import com.cmc.meeron.workspaceuser.application.port.in.response.UserResponseDto;
import com.cmc.meeron.workspaceuser.application.port.in.response.WorkspaceUserCommandResponseDto;
import com.cmc.meeron.workspaceuser.application.port.in.response.WorkspaceUserQueryResponseDto;
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
        List<WorkspaceUserQueryResponseDto> myWorkspaceUsers = workspaceUserQueryUseCase.getMyWorkspaceUsers(userId);
        return MyWorkspaceUsersResponse.fromWorkspaceUsers(myWorkspaceUsers);
    }

    @GetMapping("/workspace-users")
    @ResponseStatus(HttpStatus.OK)
    public WorkspaceUsersResponse searchWorkspaceUsers(@Valid FindWorkspaceUserRequest findWorkspaceUserRequest) {
        List<WorkspaceUserQueryResponseDto> workspaceUserQueryResponseDtos =
                workspaceUserQueryUseCase.searchWorkspaceUsers(findWorkspaceUserRequest.toRequestDto());
        return WorkspaceUsersResponse.fromWorkspaceUsers(workspaceUserQueryResponseDtos);
    }

    @GetMapping("/workspace-users/{workspaceUserId}")
    @ResponseStatus(HttpStatus.OK)
    public WorkspaceUserResponse getMyWorkspaceUser(@PathVariable Long workspaceUserId) {
        WorkspaceUserQueryResponseDto myWorkspaceUser = workspaceUserQueryUseCase.getMyWorkspaceUser(workspaceUserId);
        return WorkspaceUserResponse.fromResponseDto(myWorkspaceUser);
    }

    @GetMapping("/workspace-users/{workspaceUserId}/user")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse getUser(@PathVariable Long workspaceUserId) {
        UserResponseDto userResponseDto = workspaceUserQueryUseCase.getUser(workspaceUserId);
        return UserResponse.fromResponseDto(userResponseDto);
    }

    @PutMapping(value = "/workspace-users/{workspaceUserId}", consumes = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE
    })
    @ResponseStatus(HttpStatus.OK)
    public CreateAndModifyWorkspaceUserResponse modifyWorkspaceUser(@PathVariable Long workspaceUserId,
                                                                    @RequestPart("request") @Valid ModifyWorkspaceUserRequest modifyWorkspaceUserRequest,
                                                                    @RequestPart(value = "files", required = false) MultipartFile file) {
        WorkspaceUserCommandResponseDto responseDto = workspaceUserCommandUseCase
                .modifyWorkspaceUser(modifyWorkspaceUserRequest.toRequestDto(workspaceUserId, file));
        return CreateAndModifyWorkspaceUserResponse.fromResponseDto(responseDto);
    }

    @GetMapping("/teams/{teamId}/workspace-users")
    @ResponseStatus(HttpStatus.OK)
    public WorkspaceUsersResponse getTeamUsers(@PathVariable Long teamId) {
        List<WorkspaceUserQueryResponseDto> workspaceUserQueryResponseDtos = workspaceUserQueryUseCase.getTeamUsers(teamId);
        return WorkspaceUsersResponse.fromWorkspaceUsers(workspaceUserQueryResponseDtos);
    }

    @GetMapping("/teams/none/workspace-users")
    @ResponseStatus(HttpStatus.OK)
    public WorkspaceUsersResponse getNoneTeamUsers(@Valid FindNoneTeamWorkspaceUsersParameters findNoneTeamWorkspaceUsersParameters) {
        List<WorkspaceUserQueryResponseDto> noneTeamWorkspaceUsers = workspaceUserQueryUseCase
                .getNoneTeamWorkspaceUsers(findNoneTeamWorkspaceUsersParameters.getWorkspaceId());
        return WorkspaceUsersResponse.fromWorkspaceUsers(noneTeamWorkspaceUsers);
    }


    @PatchMapping("/teams/{teamId}/workspace-users")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void joinTeam(@PathVariable Long teamId,
                         @RequestBody @Valid JoinTeamUsersRequest joinTeamUsersRequest) {
        workspaceUserCommandUseCase.joinTeamUsers(joinTeamUsersRequest.toRequestDto(teamId));
    }

    @PatchMapping("workspace-users/{workspaceUserId}/team")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void kickOutTeamUser(@PathVariable Long workspaceUserId,
                                @RequestBody @Valid KickOutTeamUserRequest kickOutTeamUserRequest) {
        workspaceUserCommandUseCase.kickOutTeamUser(kickOutTeamUserRequest.toRequestDto(workspaceUserId));
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
    public CreateAndModifyWorkspaceUserResponse createWorkspaceUserAdmin(@RequestPart("request") @Valid CreateWorkspaceUserRequest createWorkspaceUserRequest,
                                                                         @RequestPart(value = "files", required = false) MultipartFile file,
                                                                         @AuthenticationPrincipal AuthUser authUser) {
        WorkspaceUserCommandResponseDto workspaceUserCommandResponseDto =
                workspaceUserCommandUseCase.createWorkspaceUser(createWorkspaceUserRequest.toAdminRequestDto(file, authUser.getUserId()));
        return CreateAndModifyWorkspaceUserResponse.fromResponseDto(workspaceUserCommandResponseDto);
    }

    @PostMapping(value = "/workspace-users", consumes = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE
    })
    @ResponseStatus(HttpStatus.OK)
    public CreateAndModifyWorkspaceUserResponse createWorkspaceUser(@RequestPart("request") @Valid CreateWorkspaceUserRequest createWorkspaceUserRequest,
                                                                    @RequestPart(value = "files", required = false) MultipartFile file,
                                                                    @AuthenticationPrincipal AuthUser authUser) {
        WorkspaceUserCommandResponseDto workspaceUserCommandResponseDto =
                workspaceUserCommandUseCase.createWorkspaceUser(createWorkspaceUserRequest.toRequestDto(file, authUser.getUserId()));
        return CreateAndModifyWorkspaceUserResponse.fromResponseDto(workspaceUserCommandResponseDto);
    }
}
