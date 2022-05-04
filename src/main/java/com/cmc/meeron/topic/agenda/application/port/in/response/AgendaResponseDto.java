package com.cmc.meeron.topic.agenda.application.port.in.response;

import com.cmc.meeron.topic.agenda.domain.Agenda;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgendaResponseDto {

    private Long agendaId;
    private int agendaOrder;
    private String agendaName;
    private String agendaResult;

    public static List<AgendaResponseDto> from(List<Agenda> agendas) {
        return agendas.stream()
                .map(AgendaResponseDto::from)
                .collect(Collectors.toList());
    }

    public static AgendaResponseDto from(Agenda agenda) {
        return AgendaResponseDto.builder()
                .agendaId(agenda.getId())
                .agendaOrder(agenda.getAgendaOrder())
                .agendaName(agenda.getName())
                .agendaResult(agenda.getAgendaResult())
                .build();
    }
}
