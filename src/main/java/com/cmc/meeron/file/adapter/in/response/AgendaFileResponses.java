package com.cmc.meeron.file.adapter.in.response;

import com.cmc.meeron.file.application.port.in.response.AgendaFileResponseDto;
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
public class AgendaFileResponses {

    @Builder.Default
    private List<AgendaFileResponse> files = new ArrayList<>();

    public static AgendaFileResponses from(List<AgendaFileResponseDto> responseDtos) {
        return AgendaFileResponses.builder()
                .files(responseDtos.stream()
                        .map(AgendaFileResponse::from)
                        .collect(Collectors.toList()))
                .build();
    }
}
