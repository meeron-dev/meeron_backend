package com.cmc.meeron.meeting.adapter.in.response;

import com.cmc.meeron.meeting.application.port.in.response.CreateAgendaResponseDto;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateAgendaResponse {

    @Builder.Default
    private List<AgendaResponse> agendaResponses = new ArrayList<>();

    public static CreateAgendaResponse of(List<CreateAgendaResponseDto> responseDtos) {
        return CreateAgendaResponse.builder()
                .agendaResponses(responseDtos.stream()
                        .map(CreateAgendaResponse::ofAgendaResponse)
                        .collect(Collectors.toList()))
                .build();
    }

    private static AgendaResponse ofAgendaResponse(CreateAgendaResponseDto responseDto) {
        return AgendaResponse.builder()
                .agendaNumber(responseDto.getAgendaNumber())
                .createdAgendaId(responseDto.getCreatedAgendaId())
                .build();
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AgendaResponse {
        private int agendaNumber;
        private Long createdAgendaId;
    }
}
