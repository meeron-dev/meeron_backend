package com.cmc.meeron.meeting.integration;

import com.cmc.meeron.support.IntegrationTest;
import com.cmc.meeron.support.security.WithMockJwt;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockJwt
public class MeetingIntegrationTest extends IntegrationTest {

    @DisplayName("오늘의 회의 조회 - 성공")
    @Test
    void today_meetings_list_success() throws Exception {

        // given
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("workspaceId", "1");
        params.add("workspaceUserId", "1");

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/meetings/today")
                .params(params))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meetings", hasSize(0)));
    }
}
