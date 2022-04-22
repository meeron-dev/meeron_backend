package com.cmc.meeron.team.adapter.in.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatedTeamResponse {

    private Long createdTeamId;

    public static CreatedTeamResponse of(Long teamId) {
        return CreatedTeamResponse.builder()
                .createdTeamId(teamId)
                .build();
    }
}
