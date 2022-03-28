package com.cmc.meeron.team.adapter.in;

import com.cmc.meeron.team.adapter.in.request.CreateTeamRequest;
import com.cmc.meeron.team.adapter.in.request.DeleteTeamRequest;
import com.cmc.meeron.team.adapter.in.request.ModifyTeamNameRequest;
import com.cmc.meeron.team.adapter.in.response.TeamResponse;
import com.cmc.meeron.team.adapter.in.response.WorkspaceTeamsResponse;
import com.cmc.meeron.team.application.port.in.TeamCommandUseCase;
import com.cmc.meeron.team.application.port.in.TeamQueryUseCase;
import com.cmc.meeron.team.application.port.in.response.WorkspaceTeamsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/teams")
public class TeamRestController {

    private final TeamQueryUseCase teamQueryUseCase;
    private final TeamCommandUseCase teamCommandUseCase;

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public WorkspaceTeamsResponse getWorkspaceTeams(@RequestParam("workspaceId") Long workspaceId) {
        List<WorkspaceTeamsResponseDto> workspaceTeams = teamQueryUseCase.getWorkspaceTeams(workspaceId);
        return WorkspaceTeamsResponse.of(workspaceTeams);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.OK)
    public TeamResponse createTeam(@RequestBody @Valid CreateTeamRequest createTeamRequest) {
        Long teamId = teamCommandUseCase.createTeam(createTeamRequest.toRequestDto());
        return TeamResponse.of(teamId);
    }

    @PostMapping("/{teamId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTeam(@PathVariable Long teamId,
                           @RequestBody @Valid DeleteTeamRequest deleteTeamRequest) {
        teamCommandUseCase.deleteTeam(deleteTeamRequest.toRequestDto(teamId));
    }

    @PatchMapping("/{teamId}/name")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void modifyTeamName(@PathVariable Long teamId,
                           @RequestBody @Valid ModifyTeamNameRequest modifyTeamNameRequest) {
        teamCommandUseCase.modifyTeamName(modifyTeamNameRequest.toRequestDto(teamId));
    }
}
