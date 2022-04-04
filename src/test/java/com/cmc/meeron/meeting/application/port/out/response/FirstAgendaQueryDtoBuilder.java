package com.cmc.meeron.meeting.application.port.out.response;

import java.util.List;

public class FirstAgendaQueryDtoBuilder {

    public static List<FirstAgendaQueryDto> buildList() {
        return List.of(
                FirstAgendaQueryDto.builder()
                        .meetingId(2L)
                        .agendaId(1L)
                        .agendaContents("테스트아젠다")
                        .build()
        );
    }
}
