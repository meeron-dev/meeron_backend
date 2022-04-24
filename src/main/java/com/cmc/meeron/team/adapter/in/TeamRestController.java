package com.cmc.meeron.team.adapter.in;

import com.cmc.meeron.team.adapter.in.request.*;
import com.cmc.meeron.team.adapter.in.response.CreatedTeamResponse;
import com.cmc.meeron.team.adapter.in.response.TeamResponse;
import com.cmc.meeron.team.adapter.in.response.WorkspaceTeamsResponse;
import com.cmc.meeron.team.application.port.in.TeamCommandUseCase;
import com.cmc.meeron.team.application.port.in.TeamMemberManageUseCase;
import com.cmc.meeron.team.application.port.in.TeamQueryUseCase;
import com.cmc.meeron.team.application.port.in.response.TeamResponseDto;
import com.cmc.meeron.team.application.port.in.response.WorkspaceTeamsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TeamRestController {

    private final TeamQueryUseCase teamQueryUseCase;
    private final TeamCommandUseCase teamCommandUseCase;
    private final TeamMemberManageUseCase teamMemberManageUseCase;

    @GetMapping("/teams")
    @ResponseStatus(HttpStatus.OK)
    public WorkspaceTeamsResponse getWorkspaceTeams(@RequestParam("workspaceId") Long workspaceId) {
        List<WorkspaceTeamsResponseDto> workspaceTeams = teamQueryUseCase.getWorkspaceTeams(workspaceId);
        return WorkspaceTeamsResponse.of(workspaceTeams);
    }

    @PostMapping("/teams")
    @ResponseStatus(HttpStatus.OK)
    public CreatedTeamResponse createTeam(@RequestBody @Valid CreateTeamRequest createTeamRequest) {
        Long teamId = teamCommandUseCase.createTeam(createTeamRequest.toRequestDto());
        return CreatedTeamResponse.of(teamId);
    }

    @PostMapping("/teams/{teamId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTeam(@PathVariable Long teamId,
                           @RequestBody @Valid DeleteTeamRequest deleteTeamRequest) {
        teamCommandUseCase.deleteTeam(deleteTeamRequest.toRequestDto(teamId));
    }

    @PatchMapping("/teams/{teamId}/name")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void modifyTeamName(@PathVariable Long teamId,
                           @RequestBody @Valid ModifyTeamNameRequest modifyTeamNameRequest) {
        teamCommandUseCase.modifyTeamName(modifyTeamNameRequest.toRequestDto(teamId));
    }

    @GetMapping("/meetings/{meetingId}/host-team")
    @ResponseStatus(HttpStatus.OK)
    public TeamResponse getMeetingHostTeam(@PathVariable Long meetingId) {
        TeamResponseDto responseDto = teamQueryUseCase.getMeetingHostTeam(meetingId);
        return TeamResponse.from(responseDto);
    }

    @PatchMapping("/teams/{teamId}/join")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void joinTeamMembers(@PathVariable Long teamId,
                                @RequestBody @Valid JoinTeamMembersRequest joinTeamMembersRequest) {
        teamMemberManageUseCase.joinTeamMembers(joinTeamMembersRequest.toRequestDto(teamId));
    }

    @PatchMapping("/teams/{teamId}/eject")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void ejectTeamMember(@PathVariable Long teamId,
                                @RequestBody @Valid EjectTeamMemberRequestV2 ejectTeamMemberRequestV2) {
        teamMemberManageUseCase.ejectTeamMember(ejectTeamMemberRequestV2.toRequestDto());
    }
}
