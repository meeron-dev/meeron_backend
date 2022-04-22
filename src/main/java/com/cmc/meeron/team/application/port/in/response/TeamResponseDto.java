package com.cmc.meeron.team.application.port.in.response;

import com.cmc.meeron.team.domain.Team;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamResponseDto {

    private Long teamId;
    private String teamName;

    public static TeamResponseDto from(Team team) {
        return TeamResponseDto.builder()
                .teamId(team.getId())
                .teamName(team.getName())
                .build();
    }

    public static List<TeamResponseDto> from(List<Team> teams) {
        return teams.stream()
                .map(TeamResponseDto::from)
                .collect(Collectors.toList());
    }
}
