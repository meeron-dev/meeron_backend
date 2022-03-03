package com.cmc.meeron.team.domain.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class WorkspaceTeamsQueryResponseDto {

    private Long teamId;
    private String teamName;

    @QueryProjection
    public WorkspaceTeamsQueryResponseDto(Long teamId, String teamName) {
        this.teamId = teamId;
        this.teamName = teamName;
    }
}
