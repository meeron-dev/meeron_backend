package com.cmc.meeron.attendee.integration;

import com.cmc.meeron.attendee.adapter.in.request.JoinAttendeesRequest;
import com.cmc.meeron.common.exception.meeting.MeetingErrorCode;
import com.cmc.meeron.meeting.application.port.out.MeetingQueryPort;
import com.cmc.meeron.meeting.domain.Meeting;
import com.cmc.meeron.support.IntegrationTest;
import com.cmc.meeron.support.security.WithMockJwt;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockJwt
class AttendeeIntegrationTest extends IntegrationTest {

    @Autowired
    MeetingQueryPort meetingQueryPort;

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
}
