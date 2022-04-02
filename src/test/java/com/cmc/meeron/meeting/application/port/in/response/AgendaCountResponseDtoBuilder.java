package com.cmc.meeron.meeting.application.port.in.response;

public class AgendaCountResponseDtoBuilder {

    public static AgendaCountResponseDto build() {
        return AgendaCountResponseDto.builder()
                .agendas(1)
                .checks(0)
                .files(2)
                .build();
    }
}
