package com.cmc.meeron.workspaceuser.adapter.in;

import com.cmc.meeron.common.security.AuthUser;
import com.cmc.meeron.team.adapter.in.request.JoinTeamMembersRequest;
import com.cmc.meeron.team.adapter.in.request.EjectTeamMemberRequest;
import com.cmc.meeron.workspaceuser.adapter.in.request.*;
import com.cmc.meeron.workspaceuser.adapter.in.response.CheckDuplicateNicknameResponse;
import com.cmc.meeron.workspaceuser.adapter.in.response.MyWorkspaceUsersResponse;
import com.cmc.meeron.workspaceuser.adapter.in.response.WorkspaceUserResponse;
import com.cmc.meeron.workspaceuser.adapter.in.response.WorkspaceUserResponses;
import com.cmc.meeron.workspaceuser.application.port.in.WorkspaceUserCommandUseCase;
import com.cmc.meeron.workspaceuser.application.port.in.WorkspaceUserQueryUseCase;
import com.cmc.meeron.workspaceuser.application.port.in.response.WorkspaceUserResponseDto;
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
        List<WorkspaceUserResponseDto> myWorkspaceUsers = workspaceUserQueryUseCase.getMyWorkspaceUsers(userId);
        return MyWorkspaceUsersResponse.fromWorkspaceUsers(myWorkspaceUsers);
    }

    @GetMapping("/workspace-users")
    @ResponseStatus(HttpStatus.OK)
    public WorkspaceUserResponses searchWorkspaceUsers(@Valid FindWorkspaceUserRequest findWorkspaceUserRequest) {
        List<WorkspaceUserResponseDto> workspaceUserResponseDtos =
                workspaceUserQueryUseCase.searchWorkspaceUsers(findWorkspaceUserRequest.toRequestDto());
        return WorkspaceUserResponses.fromWorkspaceUsers(workspaceUserResponseDtos);
    }

    @GetMapping("/workspace-users/{workspaceUserId}")
    @ResponseStatus(HttpStatus.OK)
    public WorkspaceUserResponse getMyWorkspaceUser(@PathVariable Long workspaceUserId) {
        WorkspaceUserResponseDto myWorkspaceUser = workspaceUserQueryUseCase.getMyWorkspaceUser(workspaceUserId);
        return WorkspaceUserResponse.from(myWorkspaceUser);
    }

    @PutMapping(value = "/workspace-users/{workspaceUserId}", consumes = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE
    })
    @ResponseStatus(HttpStatus.OK)
    public WorkspaceUserResponse modifyWorkspaceUser(@PathVariable Long workspaceUserId,
                                                     @RequestPart("request") @Valid ModifyWorkspaceUserRequest modifyWorkspaceUserRequest,
                                                     @RequestPart(value = "files", required = false) MultipartFile file) {
        WorkspaceUserResponseDto responseDto = workspaceUserCommandUseCase
                .modifyWorkspaceUser(modifyWorkspaceUserRequest.toRequestDto(workspaceUserId, file));
        return WorkspaceUserResponse.from(responseDto);
    }

    @GetMapping("/teams/{teamId}/workspace-users")
    @ResponseStatus(HttpStatus.OK)
    public WorkspaceUserResponses getTeamUsers(@PathVariable Long teamId) {
        List<WorkspaceUserResponseDto> workspaceUserResponseDtos = workspaceUserQueryUseCase.getTeamUsers(teamId);
        return WorkspaceUserResponses.fromWorkspaceUsers(workspaceUserResponseDtos);
    }

    @GetMapping("/teams/none/workspace-users")
    @ResponseStatus(HttpStatus.OK)
    public WorkspaceUserResponses getNoneTeamUsers(@Valid FindNoneTeamWorkspaceUsersParameters findNoneTeamWorkspaceUsersParameters) {
        List<WorkspaceUserResponseDto> noneTeamWorkspaceUsers = workspaceUserQueryUseCase
                .getNoneTeamWorkspaceUsers(findNoneTeamWorkspaceUsersParameters.getWorkspaceId());
        return WorkspaceUserResponses.fromWorkspaceUsers(noneTeamWorkspaceUsers);
    }

    @Deprecated
    @PatchMapping("/teams/{teamId}/workspace-users")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void joinTeam(@PathVariable Long teamId,
                         @RequestBody @Valid JoinTeamMembersRequest joinTeamMembersRequest) {
        workspaceUserCommandUseCase.joinTeamUsers(joinTeamMembersRequest.toRequestDto(teamId));
    }

    @Deprecated
    @PatchMapping("workspace-users/{workspaceUserId}/team")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void kickOutTeamUser(@PathVariable Long workspaceUserId,
                                @RequestBody @Valid EjectTeamMemberRequest ejectTeamMemberRequest) {
        workspaceUserCommandUseCase.kickOutTeamUser(ejectTeamMemberRequest.toRequestDto(workspaceUserId));
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
    public WorkspaceUserResponse createWorkspaceUserAdmin(@RequestPart("request") @Valid CreateWorkspaceUserRequest createWorkspaceUserRequest,
                                                                         @RequestPart(value = "files", required = false) MultipartFile file,
                                                                         @AuthenticationPrincipal AuthUser authUser) {
        WorkspaceUserResponseDto workspaceUserResponseDto =
                workspaceUserCommandUseCase.createWorkspaceUser(createWorkspaceUserRequest.toAdminRequestDto(file, authUser.getUserId()));
        return WorkspaceUserResponse.from(workspaceUserResponseDto);
    }

    @PostMapping(value = "/workspace-users", consumes = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE
    })
    @ResponseStatus(HttpStatus.OK)
    public WorkspaceUserResponse createWorkspaceUser(@RequestPart("request") @Valid CreateWorkspaceUserRequest createWorkspaceUserRequest,
                                                                    @RequestPart(value = "files", required = false) MultipartFile file,
                                                                    @AuthenticationPrincipal AuthUser authUser) {
        WorkspaceUserResponseDto workspaceUserResponseDto =
                workspaceUserCommandUseCase.createWorkspaceUser(createWorkspaceUserRequest.toRequestDto(file, authUser.getUserId()));
        return WorkspaceUserResponse.from(workspaceUserResponseDto);
    }
}
