package com.cmc.meeron.team.presentation.dto.response;

import com.cmc.meeron.team.application.dto.response.WorkspaceTeamsResponseDto;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkspaceTeamsResponse {

    @Builder.Default
    private List<WorkspaceTeam> teams = new ArrayList<>();

    public static WorkspaceTeamsResponse of(List<WorkspaceTeamsResponseDto> workspaceTeamsResponseDto) {
        return WorkspaceTeamsResponse.builder()
                .teams(workspaceTeamsResponseDto.stream()
                        .map(responseDto -> WorkspaceTeam.builder()
                                .teamId(responseDto.getTeamId())
                                .teamName(responseDto.getTeamName())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class WorkspaceTeam {

        private Long teamId;
        private String teamName;
    }
}
