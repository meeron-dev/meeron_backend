package com.cmc.meeron.topic.agenda.application.port.in.response;

import com.cmc.meeron.topic.agenda.application.port.in.response.AgendaCountResponseDto;

public class AgendaCountResponseDtoBuilder {

    public static AgendaCountResponseDto build() {
        return AgendaCountResponseDto.builder()
                .agendas(1)
                .checks(0)
                .files(2)
                .build();
    }
}
