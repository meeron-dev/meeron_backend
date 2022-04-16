package com.cmc.meeron.topic.agenda.application.service;

import com.cmc.meeron.common.exception.meeting.MeetingNotFoundException;
import com.cmc.meeron.topic.agenda.application.port.in.request.CreateAgendaRequestDto;
import com.cmc.meeron.meeting.domain.Meeting;
import com.cmc.meeron.topic.agenda.application.port.in.AgendaCommandUseCase;
import com.cmc.meeron.topic.agenda.application.port.out.AgendaCommandPort;
import com.cmc.meeron.topic.agenda.application.port.out.AgendaToIssueCommandPort;
import com.cmc.meeron.topic.agenda.application.port.out.AgendaToMeetingQueryPort;
import com.cmc.meeron.topic.agenda.domain.Agenda;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
class AgendaCommandService implements AgendaCommandUseCase {

    private final AgendaCommandPort agendaCommandPort;
    private final AgendaToMeetingQueryPort agendaToMeetingQueryPort;
    private final AgendaToIssueCommandPort agendaToIssueCommandPort;

    @Override
    public List<Long> createAgendas(CreateAgendaRequestDto createAgendaRequestDto) {
        Meeting meeting = agendaToMeetingQueryPort.findById(createAgendaRequestDto.getMeetingId())
                .orElseThrow(MeetingNotFoundException::new);
        return createAgendaRequestDto.getAgendaRequestDtos()
                .stream()
                .map(dto -> {
                    Agenda agenda = agendaCommandPort.save(dto.createAgenda(meeting));
                    agendaToIssueCommandPort.save(dto.createIssues(agenda));
                    return agenda.getId();
                })
                .collect(Collectors.toList());
    }
}
