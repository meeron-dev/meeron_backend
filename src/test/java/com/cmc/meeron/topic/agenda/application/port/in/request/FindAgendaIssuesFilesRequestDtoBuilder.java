package com.cmc.meeron.topic.agenda.application.port.in.request;

public class FindAgendaIssuesFilesRequestDtoBuilder {

    public static FindAgendaIssuesFilesRequestDto build() {
        return FindAgendaIssuesFilesRequestDto.builder()
                .agendaOrder(1)
                .meetingId(1L)
                .build();
    }
}
