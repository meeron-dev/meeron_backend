package com.cmc.meeron.team.application.port.in.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateTeamRequestDto {

    private Long workspaceId;
    private String teamName;
}
