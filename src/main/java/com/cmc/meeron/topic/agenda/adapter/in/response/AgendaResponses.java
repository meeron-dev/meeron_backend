package com.cmc.meeron.topic.agenda.adapter.in.response;

import com.cmc.meeron.topic.agenda.application.port.in.response.AgendaResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgendaResponses {

    @Builder.Default
    private List<AgendaResponse> agendas = new ArrayList<>();

    public static AgendaResponses from(List<AgendaResponseDto> responseDtos) {
        return AgendaResponses.builder()
                .agendas(responseDtos.stream()
                        .map(AgendaResponse::from)
                        .collect(Collectors.toList()))
                .build();
    }
}
