package com.cmc.meeron.meeting.integration;

import com.cmc.meeron.meeting.adapter.in.request.CreateMeetingRequest;
import com.cmc.meeron.meeting.application.port.out.MeetingQueryPort;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockJwt
public class MeetingCommandIntegrationTest extends IntegrationTest {

    @Autowired
    MeetingQueryPort meetingQueryPort;

    @DisplayName("회의 생성 - 성공")
    @Test
    void create_meeting_success() throws Exception {

        // given
        CreateMeetingRequest request = createCreateMeetingRequest();

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/meetings")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.meetingId", is(5)));

        List<Meeting> meetings = meetingQueryPort.findDayMeetings("WORKSPACE", List.of(1L), LocalDate.now().plusDays(1));
        assertEquals(1, meetings.size());
    }

    private CreateMeetingRequest createCreateMeetingRequest() {
        return CreateMeetingRequest.builder()
                .meetingDate(LocalDate.now().plusDays(1))
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(11, 0))
                .meetingName("테스트 회의")
                .meetingPurpose("테스트 회의 성격")
                .operationTeamId(1L)
                .meetingAdminIds(List.of(1L, 2L))
                .build();
    }
}
