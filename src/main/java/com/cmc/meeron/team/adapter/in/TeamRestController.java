package com.cmc.meeron.team.adapter.in;

import com.cmc.meeron.team.adapter.in.response.WorkspaceTeamsResponse;
import com.cmc.meeron.team.application.port.in.TeamQueryUseCase;
import com.cmc.meeron.team.application.port.in.response.WorkspaceTeamsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/teams")
public class TeamRestController {

    private final TeamQueryUseCase teamQueryUseCase;

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public WorkspaceTeamsResponse getWorkspaceTeams(@RequestParam("workspaceId") Long workspaceId) {
        List<WorkspaceTeamsResponseDto> workspaceTeams = teamQueryUseCase.getWorkspaceTeams(workspaceId);
        return WorkspaceTeamsResponse.of(workspaceTeams);
    }
}
