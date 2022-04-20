package com.cmc.meeron.team.application.port.in.response;

import com.cmc.meeron.team.domain.Team;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamResponseDto {

    private Long teamId;
    private String teamName;

    public static TeamResponseDto fromTeamIdName(Long teamId, String teamName) {
        return TeamResponseDto.builder()
                .teamId(teamId)
                .teamName(teamName)
                .build();
    }

    public static TeamResponseDto fromEntity(Team team) {
        return TeamResponseDto.builder()
                .teamId(team.getId())
                .teamName(team.getName())
                .build();
    }
}
