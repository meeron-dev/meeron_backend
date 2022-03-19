package com.cmc.meeron.team.adapter.in.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamResponse {

    private Long createdTeamId;

    public static TeamResponse of(Long teamId) {
        return TeamResponse.builder()
                .createdTeamId(teamId)
                .build();
    }
}
