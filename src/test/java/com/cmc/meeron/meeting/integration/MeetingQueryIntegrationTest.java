package com.cmc.meeron.meeting.integration;

import com.cmc.meeron.common.util.LocalDateTimeUtil;
import com.cmc.meeron.support.IntegrationTest;
import com.cmc.meeron.support.security.WithMockJwt;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.YearMonth;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockJwt
public class MeetingQueryIntegrationTest extends IntegrationTest {

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

    @DisplayName("회의 날짜 조회 - 워크스페이스의 경우")
    @Test
    void get_meeting_days_success_workspace() throws Exception {

        // given
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("type", "workspace");
        params.add("id", "1");
        params.add("date", LocalDateTimeUtil.convertYearMonth(YearMonth.of(2022, 2)));

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/meetings/days")
                .params(params)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.days", hasSize(3)));
    }

    @DisplayName("회의 날짜 조회 - 팀의 경우")
    @Test
    void get_meeting_days_success_team() throws Exception {

        // given
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("type", "team");
        params.add("id", "3");
        params.add("date", LocalDateTimeUtil.convertYearMonth(YearMonth.of(2022, 3)));

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/meetings/days")
                .params(params)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.days", hasSize(2)));
    }

    @DisplayName("회의 날짜 조회 - 워크스페이스 유저의 경우")
    @Test
    void get_meeting_days_success_workspace_users() throws Exception {

        // given
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("type", "workspace_user");
        params.add("id", "1");
        params.add("date", LocalDateTimeUtil.convertYearMonth(YearMonth.of(2022, 3)));

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/meetings/days")
                .params(params)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.days", hasSize(2)));
    }

    @DisplayName("선택한 날짜의 회의 조회 - 워크스페이스의 경우")
    @Test
    void get_meeting_day_success_workspace() throws Exception {

        // given
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("type", "workspace");
        params.add("id", "1");
        params.add("date", "2022/2/18");

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/meetings/day")
                .params(params)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meetings", hasSize(1)));
    }

    @DisplayName("선택한 날짜의 회의 조회 - 팀의 경우")
    @Test
    void get_meeting_day_success_team() throws Exception {

        // given
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("type", "team");
        params.add("id", "1");
        params.add("date", "2022/2/18");

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/meetings/day")
                .params(params)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meetings", hasSize(1)));
    }

    @DisplayName("선택한 날짜의 회의 조회 - 워크스페이스 유저의 경우")
    @Test
    void get_meeting_day_success_workspace_user() throws Exception {

        // given
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("type", "workspace_user");
        params.add("id", "1");
        params.add("date", "2022/2/18");

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/meetings/day")
                .params(params)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meetings", hasSize(1)));
    }

    @DisplayName("년도별 회의 갯수 조회 - 워크스페이스의 경우")
    @Test
    void get_year_meetings_count_workspace() throws Exception {

        // given
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("type", "workspace");
        params.add("id", "1");

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/meetings/years")
                .params(params)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.yearCounts", hasSize(1)))
                .andExpect(jsonPath("$.yearCounts[0].year", is(2022)))
                .andExpect(jsonPath("$.yearCounts[0].count", is(5)));
    }

    @DisplayName("년도별 회의 갯수 조회 - 팀의 경우")
    @Test
    void get_year_meetings_count_team() throws Exception {

        // given
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("type", "team");
        params.add("id", "1");

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/meetings/years")
                .params(params)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.yearCounts", hasSize(1)))
                .andExpect(jsonPath("$.yearCounts[0].year", is(2022)))
                .andExpect(jsonPath("$.yearCounts[0].count", is(1)));
    }

    @DisplayName("년도별 회의 갯수 조회 - 워크스페이스 유저의 경우")
    @Test
    void get_year_meetings_count_workspace_user() throws Exception {

        // given
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("type", "workspace_user");
        params.add("id", "1");

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/meetings/years")
                .params(params)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.yearCounts", hasSize(1)))
                .andExpect(jsonPath("$.yearCounts[0].year", is(2022)))
                .andExpect(jsonPath("$.yearCounts[0].count", is(5)));
    }

    @DisplayName("선택한 년도의 회의 갯수 조회 - 워크스페이스의 경우")
    @Test
    void get_month_meetings_count_workspace() throws Exception {

        // given
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("type", "workspace");
        params.add("id", "1");
        params.add("year", "2022");

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/meetings/months")
                .params(params)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.monthCounts", hasSize(12)))
                .andExpect(jsonPath("$.monthCounts[1].month", is(2)))
                .andExpect(jsonPath("$.monthCounts[1].count", is(3)))
                .andExpect(jsonPath("$.monthCounts[2].month", is(3)))
                .andExpect(jsonPath("$.monthCounts[2].count", is(2)));
    }

    @DisplayName("선택한 년도의 회의 갯수 조회 - 팀의 경우")
    @Test
    void get_month_meetings_count_team() throws Exception {

        // given
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("type", "team");
        params.add("id", "1");
        params.add("year", "2022");

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/meetings/months")
                .params(params)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.monthCounts", hasSize(12)))
                .andExpect(jsonPath("$.monthCounts[1].month", is(2)))
                .andExpect(jsonPath("$.monthCounts[1].count", is(1)));
    }

    @DisplayName("선택한 년도의 회의 갯수 조회 - 워크스페이스 유저의 경우")
    @Test
    void get_month_meetings_count_workspace_users() throws Exception {

        // given
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("type", "workspace_user");
        params.add("id", "1");
        params.add("year", "2022");

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/meetings/months")
                .params(params)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.monthCounts", hasSize(12)))
                .andExpect(jsonPath("$.monthCounts[1].month", is(2)))
                .andExpect(jsonPath("$.monthCounts[1].count", is(3)))
                .andExpect(jsonPath("$.monthCounts[2].month", is(3)))
                .andExpect(jsonPath("$.monthCounts[2].count", is(2)));
    }
}
