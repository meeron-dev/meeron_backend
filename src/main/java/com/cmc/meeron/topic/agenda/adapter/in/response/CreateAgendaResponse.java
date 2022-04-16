package com.cmc.meeron.topic.agenda.adapter.in.response;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateAgendaResponse {

    @Builder.Default
    private List<Long> createdAgendaIds = new ArrayList<>();

    public static CreateAgendaResponse of(List<Long> createdAgendaIds) {
        return CreateAgendaResponse.builder()
                .createdAgendaIds(createdAgendaIds)
                .build();
    }
}
