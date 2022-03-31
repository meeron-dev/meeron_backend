package com.cmc.meeron.meeting.application.port.in.response;

public class AgendaCountResponseDtoBuilder {

    public static AgendaCountResponseDto build() {
        return AgendaCountResponseDto.builder()
                .active(true)
                .checks(0)
                .files(2)
                .build();
    }
}
