package com.cmc.meeron.topic.agenda.integration;

import com.cmc.meeron.support.IntegrationTest;
import com.cmc.meeron.support.security.WithMockJwt;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.hasSize;
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

    @DisplayName("아젠다 상세 조회 - 성공")
    @Test
    void get_agenda_issues_files_success() throws Exception {

        // given, when, then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/meetings/{meetingId}/agendas/{agendaOrder}", "5", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.agendaId", is(1)))
                .andExpect(jsonPath("$.agendaName", is("테스트아젠다1")))
                .andExpect(jsonPath("$.issues", hasSize(1)))
                .andExpect(jsonPath("$.issues[0].issueId", is(1)))
                .andExpect(jsonPath("$.issues[0].content", is("테스트이슈1")))
                .andExpect(jsonPath("$.files", hasSize(1)))
                .andExpect(jsonPath("$.files[0].fileId", is(1)))
                .andExpect(jsonPath("$.files[0].fileName", is("테스트사진.jpg")))
                .andExpect(jsonPath("$.files[0].fileUrl", is("test-url.com")));
    }
}
