package com.cmc.meeron.attendee.integration;

import com.cmc.meeron.attendee.adapter.in.request.JoinAttendeesRequest;
import com.cmc.meeron.common.exception.meeting.MeetingErrorCode;
import com.cmc.meeron.meeting.application.port.out.MeetingQueryPort;
import com.cmc.meeron.meeting.domain.Meeting;
import com.cmc.meeron.support.IntegrationTest;
import com.cmc.meeron.support.TestImproved;
import com.cmc.meeron.support.security.WithMockJwt;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockJwt
class AttendeeIntegrationTest extends IntegrationTest {

    @Autowired
    MeetingQueryPort meetingQueryPort;

    @Deprecated
    @DisplayName("회의 참가자 조회 - 성공")
    @Test
    void get_meeting_attendees() throws Exception {

        // given, when, then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/meetings/{meetingId}/attendees/teams", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.attendees", hasSize(3)))
                .andExpect(jsonPath("$.attendees[0].teamId", is(1)))
                .andExpect(jsonPath("$.attendees[0].attends", is(1)))
                .andExpect(jsonPath("$.attendees[1].teamId", is(2)))
                .andExpect(jsonPath("$.attendees[1].attends", is(1)))
                .andExpect(jsonPath("$.attendees[2].teamId", is(3)))
                .andExpect(jsonPath("$.attendees[2].attends", is(3)));
    }

    @TestImproved(originMethod = "get_meeting_attendees")
    @DisplayName("회의 참가자 조회 - 성공")
    @Test
    void get_meeting_attendees_counts_by_team_success() throws Exception {

        // given, when, then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/meetings/{meetingId}/attendees/counts", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.attendeesCountByTeam", hasSize(3)))
                .andExpect(jsonPath("$.attendeesCountByTeam[0].team.teamId", is(1)))
                .andExpect(jsonPath("$.attendeesCountByTeam[0].count.attend", is(1)))
                .andExpect(jsonPath("$.attendeesCountByTeam[1].team.teamId", is(2)))
                .andExpect(jsonPath("$.attendeesCountByTeam[1].count.attend", is(1)))
                .andExpect(jsonPath("$.attendeesCountByTeam[2].team.teamId", is(3)))
                .andExpect(jsonPath("$.attendeesCountByTeam[2].count.attend", is(3)));
    }

    @Deprecated
    @DisplayName("회의 참가자 팀별 상세 조회 - 성공")
    @Test
    void get_meeting_teams_attendees() throws Exception {

        // given, when, then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/meetings/{meetingId}/attendees/teams/{teamId}", 1, 3)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.attends", hasSize(3)))
                .andExpect(jsonPath("$.absents", hasSize(0)))
                .andExpect(jsonPath("$.unknowns", hasSize(0)));
    }

    @TestImproved(originMethod = "get_meeting_teams_attendees")
    @DisplayName("회의 참가자 팀별 상세 조회 - 성공")
    @Test
    void get_meeting_team_attendees_v2() throws Exception {

        // given, when, then, docs
        mockMvc.perform(MockMvcRequestBuilders.get("/api/meetings/{meetingId}/teams/{teamId}/attendees", 1, 3)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.attends", hasSize(3)))
                .andExpect(jsonPath("$.absents", hasSize(0)))
                .andExpect(jsonPath("$.unknowns", hasSize(0)));
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

    @Sql("classpath:attendee-test.sql")
    @DisplayName("회의 참가자 상태 변경 - 성공")
    @Test
    void change_attendee_status_success() throws Exception {

        // given, when, then
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/attendees/{attendeeId}/{status}",
                1, "absent")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @DisplayName("회의 관리자 조회 - 성공")
    @Test
    void get_meeting_admins_success() throws Exception {

        // given, when, then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/meetings/{meetingId}/admins", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.admins", hasSize(2)));
    }

    @DisplayName("내가 참여한 회의의 참가자 정보 조회 - 성공")
    @Test
    void get_my_attendee_success() throws Exception {

        // given, when, then
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("workspaceUserId", "1");
        mockMvc.perform(MockMvcRequestBuilders.get("/api/meetings/{meetingId}/attendees/me",
                "1")
                .params(params)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.attendeeId", is(1)))
                .andExpect(jsonPath("$.meetingId", is(1)))
                .andExpect(jsonPath("$.attendStatus", is("ATTEND")))
                .andExpect(jsonPath("$.meetingAdmin", is(false)))
                .andExpect(jsonPath("$.workspaceUser.workspaceUserId", is(1)))
                .andExpect(jsonPath("$.workspaceUser.workspaceId", is(1)))
                .andExpect(jsonPath("$.workspaceUser.workspaceAdmin", is(false)))
                .andExpect(jsonPath("$.workspaceUser.nickname", is("무무")))
                .andExpect(jsonPath("$.workspaceUser.position", is("Server / PM")))
                .andExpect(jsonPath("$.workspaceUser.profileImageUrl", emptyString()))
                .andExpect(jsonPath("$.workspaceUser.email", nullValue()))
                .andExpect(jsonPath("$.workspaceUser.phone", nullValue()));
    }
}
