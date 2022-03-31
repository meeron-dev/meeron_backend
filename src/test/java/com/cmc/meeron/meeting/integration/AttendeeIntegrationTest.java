package com.cmc.meeron.meeting.integration;

import com.cmc.meeron.support.IntegrationTest;
import com.cmc.meeron.support.security.WithMockJwt;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockJwt
class AttendeeIntegrationTest extends IntegrationTest {

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
}
