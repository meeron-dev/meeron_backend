package com.cmc.meeron.team.adapter.in.response;

import com.cmc.meeron.team.application.port.in.response.TeamResponseDto;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamResponses {

    @Builder.Default
    private List<TeamResponse> teams = new ArrayList<>();

    public static TeamResponses of(List<TeamResponseDto> teamResponseDtos) {
        return TeamResponses.builder()
                .teams(teamResponseDtos
                        .stream()
                        .map(TeamResponse::from)
                        .collect(Collectors.toList()))
                .build();
    }
}
