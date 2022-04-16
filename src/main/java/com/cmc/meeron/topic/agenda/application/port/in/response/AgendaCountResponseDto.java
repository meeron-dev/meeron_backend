package com.cmc.meeron.topic.agenda.application.port.in.response;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgendaCountResponseDto {

    private long agendas;
    private long checks;
    private long files;

    public static AgendaCountResponseDto notFound() {
        return AgendaCountResponseDto.builder()
                .agendas(0)
                .checks(0)
                .files(0)
                .build();
    }

    public static AgendaCountResponseDto found(long agendaCount, long fileCount) {
        return AgendaCountResponseDto.builder()
                .agendas(agendaCount)
                .checks(0)
                .files(fileCount)
                .build();
    }
}
