package com.cmc.meeron.team.application.port.in.request;

public class SetTeamNameRequestDtoBuilder {

    public static ModifyTeamNameRequestDto build() {
        return ModifyTeamNameRequestDto.builder()
                .teamId(1L)
                .name("테스트")
                .build();
    }
}
