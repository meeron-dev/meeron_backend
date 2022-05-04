package com.cmc.meeron.team.application.port.in.response;

import java.util.List;

public class TeamResponseDtoBuilder {

    public static TeamResponseDto build() {
        return TeamResponseDto.builder()
                .teamId(1L)
                .teamName("테스트팀")
                .build();
    }

    public static List<TeamResponseDto> buildList() {
        return List.of(
                TeamResponseDto.builder()
                        .teamId(1L)
                        .teamName("테스트팀")
                        .build(),
                TeamResponseDto.builder()
                        .teamId(2L)
                        .teamName("테스투팀")
                        .build()
        );
    }
}
