package com.cmc.meeron.meeting.application.service;

import com.cmc.meeron.common.exception.meeting.AgendaNotFoundException;
import com.cmc.meeron.file.application.port.out.AgendaFileQueryPort;
import com.cmc.meeron.meeting.application.port.in.request.FindAgendaIssuesFilesRequestDto;
import com.cmc.meeron.meeting.application.port.in.request.FindAgendaIssuesFilesRequestDtoBuilder;
import com.cmc.meeron.meeting.application.port.in.response.AgendaCountResponseDto;
import com.cmc.meeron.meeting.application.port.in.response.AgendaIssuesFilesResponseDto;
import com.cmc.meeron.meeting.application.port.out.AgendaQueryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.cmc.meeron.meeting.AgendaFileFixture.AGENDA_FILE_1;
import static com.cmc.meeron.meeting.AgendaFixture.AGENDA;
import static com.cmc.meeron.meeting.IssueFixture.ISSUE_1;
import static com.cmc.meeron.meeting.IssueFixture.ISSUE_2;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgendaQueryServiceTest {

    @Mock
    AgendaQueryPort agendaQueryPort;
    @Mock
    AgendaFileQueryPort agendaFileQueryPort;
    @InjectMocks
    AgendaQueryService agendaQueryService;

    @DisplayName("아젠다 수, 체크 수, 파일 수 조회 - 성공 / 아젠다가 없을 경우")
    @Test
    void get_agenda_counts_by_meeting_id_success_not_found_agendas() throws Exception {

        // given
        when(agendaQueryPort.countsByMeetingId(any()))
                .thenReturn(0L);

        // when
        AgendaCountResponseDto responseDto = agendaQueryService.getAgendaCountsByMeetingId(1L);

        // then
        assertAll(
                () -> verify(agendaQueryPort).countsByMeetingId(1L),
                () -> verify(agendaFileQueryPort, times(0)).countByMeetingId(1L),
                () -> assertEquals(0, responseDto.getAgendas()),
                () -> assertEquals(0, responseDto.getFiles()),
                () -> assertEquals(0, responseDto.getChecks())
        );
    }

    @DisplayName("아젠다 수, 체크 수, 파일 수 조회 - 성공 / 아젠다가 있을 경우")
    @Test
    void get_agenda_counts_by_meeting_id_success_found_agendas() throws Exception {

        // given
        when(agendaQueryPort.countsByMeetingId(any()))
                .thenReturn(2L);
        when(agendaFileQueryPort.countByMeetingId(any()))
                .thenReturn(1L);

        // when
        AgendaCountResponseDto responseDto = agendaQueryService.getAgendaCountsByMeetingId(1L);

        // then
        assertAll(
                () -> verify(agendaQueryPort).countsByMeetingId(1L),
                () -> verify(agendaFileQueryPort).countByMeetingId(1L),
                () -> assertEquals(2, responseDto.getAgendas()),
                () -> assertEquals(1, responseDto.getFiles())
        );
    }

    @DisplayName("아젠다 상세 조회 - 실패 / 존재하지 않는 순서일 경우")
    @Test
    void get_agenda_issue_files_fail_not_found_agenda() throws Exception {

        // given
        FindAgendaIssuesFilesRequestDto requestDto = FindAgendaIssuesFilesRequestDtoBuilder.build();
        when(agendaQueryPort.findByMeetingIdAndAgendaOrder(any(), anyInt()))
                .thenReturn(Optional.empty());

        // when, then
        assertThrows(AgendaNotFoundException.class,
                () -> agendaQueryService.getAgendaIssuesFiles(requestDto));
    }

    @DisplayName("아젠다 상세 조회 - 성공")
    @Test
    void get_agenda_issue_files_success() throws Exception {

        // given
        FindAgendaIssuesFilesRequestDto requestDto = FindAgendaIssuesFilesRequestDtoBuilder.build();
        when(agendaQueryPort.findByMeetingIdAndAgendaOrder(any(), anyInt()))
                .thenReturn(Optional.of(AGENDA));
        when(agendaQueryPort.findByAgendaId(any()))
                .thenReturn(List.of(ISSUE_1, ISSUE_2));
        when(agendaFileQueryPort.findByAgendaId(any()))
                .thenReturn(List.of(AGENDA_FILE_1));

        // when
        AgendaIssuesFilesResponseDto responseDto = agendaQueryService.getAgendaIssuesFiles(requestDto);

        // then
        assertAll(
                () -> verify(agendaQueryPort).findByMeetingIdAndAgendaOrder(requestDto.getMeetingId(), requestDto.getAgendaOrder()),
                () -> verify(agendaQueryPort).findByAgendaId(AGENDA.getId()),
                () -> verify(agendaFileQueryPort).findByAgendaId(AGENDA.getId()),
                () -> assertEquals(2, responseDto.getIssues().size()),
                () -> assertEquals(1, responseDto.getFiles().size())
        );
    }
}
