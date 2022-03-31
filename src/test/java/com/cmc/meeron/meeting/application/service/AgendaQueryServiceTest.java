package com.cmc.meeron.meeting.application.service;

import com.cmc.meeron.file.application.port.out.AgendaFileQueryPort;
import com.cmc.meeron.meeting.application.port.in.response.AgendaCountResponseDto;
import com.cmc.meeron.meeting.application.port.out.AgendaQueryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AgendaQueryServiceTest {

    @Mock
    AgendaQueryPort agendaQueryPort;
    @Mock
    AgendaFileQueryPort agendaFileQueryPort;
    @InjectMocks
    AgendaQueryService agendaQueryService;

    @DisplayName("아젠다 활성화 조회 - 성공 / 아젠다가 없을 때")
    @Test
    void get_agenda_active_by_meeting_id_success_not_exist_agenda() throws Exception {

        // given
        when(agendaQueryPort.existsByMeetingId(any()))
                .thenReturn(false);

        // when
        AgendaCountResponseDto responseDto = agendaQueryService.getAgendaCountsByMeetingId(1L);

        // then
        assertAll(
                () -> verify(agendaQueryPort).existsByMeetingId(1L),
                () -> assertFalse(responseDto.isActive()),
                () -> assertEquals(0, responseDto.getChecks()),
                () -> assertEquals(0, responseDto.getFiles())
        );
    }

    @DisplayName("아젠다 활성화 조회 - 성공 / 아젠다가 있을 때")
    @Test
    void get_agenda_active_by_meeting_id_success_exist_agenda() throws Exception {

        // given
        when(agendaQueryPort.existsByMeetingId(any()))
                .thenReturn(true);
        when(agendaFileQueryPort.countByMeetingId(any()))
                .thenReturn(2);

        // when
        AgendaCountResponseDto responseDto = agendaQueryService.getAgendaCountsByMeetingId(1L);

        // then
        assertAll(
                () -> verify(agendaQueryPort).existsByMeetingId(1L),
                () -> verify(agendaFileQueryPort).countByMeetingId(1L),
                () -> assertTrue(responseDto.isActive()),
                () -> assertEquals(0, responseDto.getChecks()),
                () -> assertEquals(2, responseDto.getFiles())
        );
    }
}
