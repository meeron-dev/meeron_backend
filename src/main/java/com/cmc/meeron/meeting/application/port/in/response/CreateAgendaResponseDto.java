package com.cmc.meeron.meeting.application.port.in.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateAgendaResponseDto {

    private int agendaNumber;
    private Long createdAgendaId;

    public static CreateAgendaResponseDto of(int agendaNumber, Long agendaId) {
        return CreateAgendaResponseDto.builder()
                .agendaNumber(agendaNumber)
                .createdAgendaId(agendaId)
                .build();
    }
}
