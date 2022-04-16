package com.cmc.meeron.topic.agenda.application.service;

import com.cmc.meeron.common.exception.meeting.MeetingNotFoundException;
import com.cmc.meeron.topic.agenda.application.port.in.request.CreateAgendaRequestDto;
import com.cmc.meeron.meeting.domain.Meeting;
import com.cmc.meeron.topic.agenda.application.port.out.AgendaCommandPort;
import com.cmc.meeron.topic.agenda.application.port.out.AgendaToIssueCommandPort;
import com.cmc.meeron.topic.agenda.application.port.out.AgendaToMeetingQueryPort;
import com.cmc.meeron.topic.agenda.domain.Agenda;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.cmc.meeron.meeting.MeetingFixture.MEETING;
import static com.cmc.meeron.topic.agenda.AgendaFixture.AGENDA1;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgendaCommandServiceTest {

    @Mock
    AgendaToMeetingQueryPort agendaToMeetingQueryPort;
    @Mock
    AgendaCommandPort agendaCommandPort;
    @Mock
    AgendaToIssueCommandPort agendaToIssueCommandPort;
    @InjectMocks
    AgendaCommandService agendaCommandService;

    private Meeting meeting;
    private Agenda agenda;

    @BeforeEach
    void setUp() {
        meeting = MEETING;
        agenda = AGENDA1;
    }

    @DisplayName("아젠다 생성 - 실패 / 회의가 존재하지 않을 경우")
    @Test
    void create_agenda_fail_not_found_meeting() throws Exception {

        // given
        CreateAgendaRequestDto requestDto = createCreateAgendaRequestDto();
        when(agendaToMeetingQueryPort.findById(any()))
                .thenThrow(new MeetingNotFoundException());

        // when, then
        assertThrows(
                MeetingNotFoundException.class,
                () -> agendaCommandService.createAgendas(requestDto)
        );
    }

    private CreateAgendaRequestDto createCreateAgendaRequestDto() {
        return CreateAgendaRequestDto.builder()
                .meetingId(1L)
                .agendaRequestDtos(List.of(
                        CreateAgendaRequestDto.AgendaRequestDto.builder()
                                .agendaName("테스트아젠다1")
                                .agendaOrder(1)
                                .issues(List.of("테스트이슈1", "테스트이슈2"))
                                .build(),
                        CreateAgendaRequestDto.AgendaRequestDto.builder()
                                .agendaName("테스트아젠다2")
                                .agendaOrder(2)
                                .issues(List.of("테스트이슈1", "테스트이슈2"))
                                .build()))
                .build();
    }

    @DisplayName("아젠다 생성 - 성공")
    @Test
    void create_agenda_success() throws Exception {

        // given
        CreateAgendaRequestDto requestDto = createCreateAgendaRequestDto();
        when(agendaToMeetingQueryPort.findById(any()))
                .thenReturn(Optional.of(meeting));
        when(agendaCommandPort.save(any(Agenda.class)))
                .thenReturn(agenda);

        // when
        List<Long> responseDtos = agendaCommandService.createAgendas(requestDto);

        // then
        assertAll(
                () -> verify(agendaCommandPort, times(requestDto.getAgendaRequestDtos().size()))
                        .save(any(Agenda.class)),
                () -> verify(agendaToIssueCommandPort, times(2))
                        .save(anyList()),
                () -> assertEquals(responseDtos.size(), requestDto.getAgendaRequestDtos().size())
        );
    }
}