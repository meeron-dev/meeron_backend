package com.cmc.meeron.topic.agenda.application.port.in;

import com.cmc.meeron.topic.agenda.application.port.in.request.CreateAgendaRequestDto;

import java.util.List;

public interface AgendaCommandUseCase {

    List<Long> createAgendas(CreateAgendaRequestDto toRequestDtoAndSortByAgendaOrder);
}
