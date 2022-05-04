package com.cmc.meeron.topic.agenda.adapter.in.response;

import com.cmc.meeron.topic.agenda.application.port.in.response.AgendaResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgendaResponse {

    private Long agendaId;
    private String agendaName;
    private int agendaOrder;
    private String agendaResult;

    public static AgendaResponse from(AgendaResponseDto agendaResponseDto) {
        return AgendaResponse.builder()
                .agendaId(agendaResponseDto.getAgendaId())
                .agendaName(agendaResponseDto.getAgendaName())
                .agendaOrder(agendaResponseDto.getAgendaOrder())
                .agendaResult(agendaResponseDto.getAgendaResult())
                .build();
    }
}
