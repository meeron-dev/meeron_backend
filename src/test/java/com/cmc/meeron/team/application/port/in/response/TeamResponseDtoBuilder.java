package com.cmc.meeron.team.application.port.in.response;

public class TeamResponseDtoBuilder {

    public static TeamResponseDto build() {
        return TeamResponseDto.builder()
                .teamId(1L)
                .teamName("테스트팀")
                .build();
    }
}
