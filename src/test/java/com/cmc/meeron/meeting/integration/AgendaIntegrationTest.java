package com.cmc.meeron.meeting.integration;

import com.cmc.meeron.support.IntegrationTest;
import com.cmc.meeron.support.security.WithMockJwt;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockJwt
class AgendaIntegrationTest extends IntegrationTest {

    @DisplayName("회의 아젠다 카운트 조회 - 성공")
    @Test
    void get_agenda_counts_success() throws Exception {

        // given, when, then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/meetings/{meetingId}/agendas/count", "5")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.agendas", is(1)))
                .andExpect(jsonPath("$.checks", is(0)))
                .andExpect(jsonPath("$.files", is(1)));
    }

    // TODO: 2022/04/02 kobeomseok95 아젠다 상세 조회 통합 테스트 추가

}
