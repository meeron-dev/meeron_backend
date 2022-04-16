package com.cmc.meeron.topic.agenda.application.port.in.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FindAgendaIssuesFilesRequestDto {

    private Long meetingId;
    private int agendaOrder;

    public static FindAgendaIssuesFilesRequestDto of(Long meetingId, int agendaOrder) {
        return FindAgendaIssuesFilesRequestDto.builder()
                .meetingId(meetingId)
                .agendaOrder(agendaOrder)
                .build();
    }
}
