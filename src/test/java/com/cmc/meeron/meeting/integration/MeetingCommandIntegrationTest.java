package com.cmc.meeron.meeting.integration;

import com.cmc.meeron.common.exception.meeting.MeetingErrorCode;
import com.cmc.meeron.meeting.adapter.in.request.CreateAgendaRequest;
import com.cmc.meeron.meeting.adapter.in.request.CreateMeetingRequest;
import com.cmc.meeron.meeting.adapter.in.request.JoinAttendeesRequest;
import com.cmc.meeron.meeting.application.port.out.MeetingMyCalendarQueryPort;
import com.cmc.meeron.meeting.application.port.out.MeetingQueryPort;
import com.cmc.meeron.meeting.application.port.out.MeetingTeamCalendarQueryPort;
import com.cmc.meeron.meeting.application.port.out.MeetingWorkspaceCalendarQueryPort;
import com.cmc.meeron.meeting.domain.Agenda;
import com.cmc.meeron.meeting.domain.Meeting;
import com.cmc.meeron.support.IntegrationTest;
import com.cmc.meeron.support.security.WithMockJwt;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

    @DisplayName("회의 참여자 추가 - 실패 / 이미 참여중인 유저가 있는 경우")
    @Test
    void join_attendees_fail_duplicate_attendees() throws Exception {

        // given
        Meeting meeting = findTestMeeting(4L);
        JoinAttendeesRequest request = createJoinAttendeesRequest();

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/meetings/{meetingId}/attendees", meeting.getId().intValue())
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(MeetingErrorCode.ATTENDEE_DUPLICATE.getCode())));
    }

    @DisplayName("회의 참여자 추가 - 성공 / 존재하지 않는 워크스페이스 유저를 참여시키는 경우")
    @Test
    void join_attendees_fail_not_found_workspace_user() throws Exception {

        // given
        Meeting meeting = findTestMeeting(4L);
        JoinAttendeesRequest request = JoinAttendeesRequest.builder()
                .workspaceUserIds(List.of(14768L, 14769L))
                .build();

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/meetings/{meetingId}/attendees", meeting.getId().intValue())
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @DisplayName("회의 참여자 추가 - 성공")
    @Test
    void join_attendees_success() throws Exception {

        // given
        Meeting meeting = findTestMeeting(5L);
        JoinAttendeesRequest request = createJoinAttendeesRequest();

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/meetings/{meetingId}/attendees", meeting.getId().intValue())
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        // then
        flushAndClear();
        Meeting afterJoinAttendeesMeeting = findTestMeeting(5L);
        assertEquals(3, afterJoinAttendeesMeeting.getAttendees().size());
    }

    private Meeting findTestMeeting(Long meetingId) {
        return meetingQueryPort.findById(meetingId).orElseThrow();
    }

    private JoinAttendeesRequest createJoinAttendeesRequest() {
        return JoinAttendeesRequest.builder()
                .workspaceUserIds(List.of(3L, 4L))
                .build();
    }

    @DisplayName("회의 아젠다 생성 - 성공")
    @Test
    void create_agenda_success() throws Exception {

        // given
        Meeting meeting = findTestMeeting(5L);
        CreateAgendaRequest request = createCreateAgendaRequest();

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/meetings/{meetingId}/agendas", meeting.getId().intValue())
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        // then
        flushAndClear();
        Meeting afterJoinAttendeesMeeting = findTestMeeting(5L);
        // FIXME: 2022/03/11 kobeomseok95 추후 리팩토링
        Agenda agenda1 = meetingQueryPort.findAgendaById(1L).orElseThrow();
        Agenda agenda2 = meetingQueryPort.findAgendaById(2L).orElseThrow();
        assertAll(
                () -> assertEquals(afterJoinAttendeesMeeting, agenda1.getMeeting()),
                () -> assertEquals(afterJoinAttendeesMeeting, agenda2.getMeeting())
        );
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
}
