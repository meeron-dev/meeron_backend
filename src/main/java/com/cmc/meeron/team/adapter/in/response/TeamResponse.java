package com.cmc.meeron.team.adapter.in.response;

import com.cmc.meeron.team.application.port.in.response.TeamResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamResponse {

    private Long teamId;
    private String teamName;

    public static TeamResponse from(TeamResponseDto responseDto) {
        return TeamResponse.builder()
                .teamId(responseDto.getTeamId())
                .teamName(responseDto.getTeamName())
                .build();
    }
}
