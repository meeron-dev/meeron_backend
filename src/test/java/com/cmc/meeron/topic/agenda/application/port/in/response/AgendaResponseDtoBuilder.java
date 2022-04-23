package com.cmc.meeron.topic.agenda.application.port.in.response;

import java.util.List;

public class AgendaResponseDtoBuilder {

    public static AgendaResponseDto build() {
        return AgendaResponseDto.builder()
                        .agendaId(1L)
                        .agendaName("테스트아젠다1")
                        .agendaOrder(1)
                        .agendaResult("아젠다결과1")
                        .build();
    }

    public static List<AgendaResponseDto> buildList() {
        return List.of(
                AgendaResponseDto.builder()
                        .agendaId(1L)
                        .agendaName("테스트아젠다1")
                        .agendaOrder(1)
                        .agendaResult("아젠다결과1")
                        .build(),
                AgendaResponseDto.builder()
                        .agendaId(2L)
                        .agendaName("테스트아젠다2")
                        .agendaOrder(2)
                        .agendaResult("아젠다결과2")
                        .build()
        );
    }
}
