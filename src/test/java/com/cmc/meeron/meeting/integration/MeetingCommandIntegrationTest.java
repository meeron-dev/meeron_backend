package com.cmc.meeron.meeting.integration;

import com.cmc.meeron.meeting.adapter.in.request.CreateAgendaRequest;
import com.cmc.meeron.meeting.adapter.in.request.CreateMeetingRequest;
import com.cmc.meeron.meeting.adapter.in.request.DeleteMeetingRequest;
import com.cmc.meeron.meeting.adapter.in.request.DeleteMeetingRequestBuilder;
import com.cmc.meeron.meeting.application.port.out.MeetingMyCalendarQueryPort;
import com.cmc.meeron.meeting.application.port.out.MeetingQueryPort;
import com.cmc.meeron.meeting.application.port.out.MeetingTeamCalendarQueryPort;
import com.cmc.meeron.meeting.application.port.out.MeetingWorkspaceCalendarQueryPort;
import com.cmc.meeron.meeting.domain.Meeting;
import com.cmc.meeron.support.IntegrationTest;
import com.cmc.meeron.support.security.WithMockJwt;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockJwt
public class MeetingCommandIntegrationTest extends IntegrationTest {

    @Autowired
    MeetingQueryPort meetingQueryPort;
    @Autowired
    MeetingWorkspaceCalendarQueryPort meetingWorkspaceCalendarQueryPort;
    @Autowired
    MeetingTeamCalendarQueryPort meetingTeamCalendarQueryPort;
    @Autowired
    MeetingMyCalendarQueryPort meetingMyCalendarQueryPort;

    @DisplayName("회의 생성 - 성공")
    @Test
    void create_meeting_success() throws Exception {

        // given
        CreateMeetingRequest request = createCreateMeetingRequest();

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/meetings")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        // then
        List<Meeting> myDayMeetings = meetingMyCalendarQueryPort.findMyDayMeetings(List.of(1L), LocalDate.now().plusDays(1));
        List<Meeting> teamDayMeetings = meetingTeamCalendarQueryPort.findTeamDayMeetings(request.getOperationTeamId(), LocalDate.now().plusDays(1));
        List<Meeting> workspaceDayMeetings = meetingWorkspaceCalendarQueryPort.findWorkspaceDayMeetings(request.getWorkspaceId(), LocalDate.now().plusDays(1));
        assertAll(
                () -> assertEquals(1, myDayMeetings.size()),
                () -> assertEquals(1, teamDayMeetings.size()),
                () -> assertEquals(1, workspaceDayMeetings.size())
        );
    }

    private CreateMeetingRequest createCreateMeetingRequest() {
        return CreateMeetingRequest.builder()
                .workspaceId(1L)
                .meetingDate(LocalDate.now().plusDays(1))
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(11, 0))
                .meetingName("테스트 회의")
                .meetingPurpose("테스트 회의 성격")
                .operationTeamId(1L)
                .meetingAdminIds(List.of(2L))
                .build();
    }

    @DisplayName("회의 아젠다 생성 - 성공")
    @Test
    void create_agenda_success() throws Exception {

        // given
        Meeting meeting = findTestMeeting(5L);
        CreateAgendaRequest request = createCreateAgendaRequest();

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/meetings/{meetingId}/agendas", meeting.getId().intValue())
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    private Meeting findTestMeeting(Long meetingId) {
        return meetingQueryPort.findById(meetingId).orElseThrow();
    }

    private CreateAgendaRequest createCreateAgendaRequest() {
        return CreateAgendaRequest.builder()
                .agendas(List.of(CreateAgendaRequest.AgendaRequest.builder()
                                .order(1)
                                .name("테스트아젠다1")
                                .issues(List.of(CreateAgendaRequest.IssueRequest.builder()
                                        .issue("테스트이슈1")
                                        .build()))
                                .build(),
                        CreateAgendaRequest.AgendaRequest.builder()
                                .order(2)
                                .name("테스트아젠다2")
                                .issues(List.of(CreateAgendaRequest.IssueRequest.builder()
                                                .issue("테스트이슈1")
                                                .build(),
                                        CreateAgendaRequest.IssueRequest.builder()
                                                .issue("테스트이슈2")
                                                .build()))
                                .build()))
                .build();
    }

    @Sql("classpath:meeting-test.sql")
    @DisplayName("회의 삭제 - 성공")
    @Test
    void delete_meeting_success() throws Exception {

        // given
        DeleteMeetingRequest request = DeleteMeetingRequestBuilder.buildIntegrationSuccessCase();

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/meetings/{meetingId}/delete", 8)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // then
        flushAndClear();
        assertTrue(meetingQueryPort.findById(8L).isEmpty());
    }
}
