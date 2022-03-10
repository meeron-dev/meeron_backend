package com.cmc.meeron.team.application.port.in.response;

import com.cmc.meeron.team.application.port.out.response.WorkspaceTeamsQueryResponseDto;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkspaceTeamsResponseDto {

    private Long teamId;
    private String teamName;

    public static List<WorkspaceTeamsResponseDto> fromQueryResponseDtos(List<WorkspaceTeamsQueryResponseDto> workspaceTeamsQueryResponseDtos) {
        return workspaceTeamsQueryResponseDtos.stream()
                .map(queryResponse -> WorkspaceTeamsResponseDto.builder()
                        .teamId(queryResponse.getTeamId())
                        .teamName(queryResponse.getTeamName())
                        .build())
                .collect(Collectors.toList());
    }
}
