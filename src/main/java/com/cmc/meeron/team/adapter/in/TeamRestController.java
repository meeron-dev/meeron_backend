package com.cmc.meeron.team.adapter.in;

import com.cmc.meeron.common.meta.Improved;
import com.cmc.meeron.team.adapter.in.request.CreateTeamRequest;
import com.cmc.meeron.team.adapter.in.request.DeleteTeamRequest;
import com.cmc.meeron.team.adapter.in.request.ModifyTeamNameRequest;
import com.cmc.meeron.team.adapter.in.response.CreatedTeamResponse;
import com.cmc.meeron.team.adapter.in.response.TeamResponse;
import com.cmc.meeron.team.adapter.in.response.TeamResponses;
import com.cmc.meeron.team.application.port.in.TeamCommandUseCase;
import com.cmc.meeron.team.application.port.in.TeamQueryUseCase;
import com.cmc.meeron.team.application.port.in.response.TeamResponseDto;
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

    @Deprecated
    @GetMapping("/teams")
    @ResponseStatus(HttpStatus.OK)
    public TeamResponses getWorkspaceTeams(@RequestParam("workspaceId") Long workspaceId) {
        List<TeamResponseDto> responseDtos = teamQueryUseCase.getWorkspaceTeams(workspaceId);
        return TeamResponses.of(responseDtos);
    }

    @Improved(originMethod = "getWorkspaceTeams")
    @GetMapping("/workspaces/{workspaceId}/teams")
    public TeamResponses getWorkspaceTeamsV2(@PathVariable Long workspaceId) {
        List<TeamResponseDto> responseDtos = teamQueryUseCase.getWorkspaceTeams(workspaceId);
        return TeamResponses.of(responseDtos);
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
}
