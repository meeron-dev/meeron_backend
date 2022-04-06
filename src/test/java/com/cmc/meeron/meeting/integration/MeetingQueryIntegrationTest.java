package com.cmc.meeron.meeting.integration;

import com.cmc.meeron.common.util.LocalDateTimeUtil;
import com.cmc.meeron.support.IntegrationTest;
import com.cmc.meeron.support.security.WithMockJwt;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.YearMonth;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockJwt
class MeetingQueryIntegrationTest extends IntegrationTest {

    @Sql("classpath:meeting-test.sql")
    @DisplayName("오늘의 회의 조회 - 성공")
    @Test
    void today_meetingst_success() throws Exception {

        // given
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("workspaceId", "1");
        params.add("workspaceUserId", "1");

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/meetings/today")
                .params(params))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meetings", hasSize(2)))
                .andExpect(jsonPath("$.meetings[0].meeting.meetingId", is(6)))
                .andExpect(jsonPath("$.meetings[0].agendas", hasSize(1)))
                .andExpect(jsonPath("$.meetings[0].admins", hasSize(2)))
                .andExpect(jsonPath("$.meetings[0].attendCount.attend", is(2)))
                .andExpect(jsonPath("$.meetings[0].attendCount.absent", is(1)))
                .andExpect(jsonPath("$.meetings[0].attendCount.unknown", is(2)))
                .andExpect(jsonPath("$.meetings[1].meeting.meetingId", is(7)))
                .andExpect(jsonPath("$.meetings[1].agendas", hasSize(0)))
                .andExpect(jsonPath("$.meetings[1].admins", hasSize(1)))
                .andExpect(jsonPath("$.meetings[1].attendCount.attend", is(2)))
                .andExpect(jsonPath("$.meetings[1].attendCount.absent", is(0)))
                .andExpect(jsonPath("$.meetings[1].attendCount.unknown", is(0)))

                ;
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

    @DisplayName("회의 상세 정보 조회 - 성공")
    @Test
    void get_meeting_success() throws Exception {

        // given, when, then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/meetings/{meetingId}", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meetingId", is(1)))
                .andExpect(jsonPath("$.meetingName", is("1주차 디자인 공지")))
                .andExpect(jsonPath("$.meetingPurpose", is("공지사항")))
                .andExpect(jsonPath("$.meetingDate", is("2022/2/18")))
                .andExpect(jsonPath("$.startTime", is("02:00 PM")))
                .andExpect(jsonPath("$.endTime", is("04:00 PM")))
                .andExpect(jsonPath("$.operationTeamId", is(1)))
                .andExpect(jsonPath("$.operationTeamName", is("디자인팀")))
                .andExpect(jsonPath("$.admins", hasSize(2)))
                .andExpect(jsonPath("$.admins[0].workspaceUserId", is(2)))
                .andExpect(jsonPath("$.admins[0].nickname", is("네코")))
                .andExpect(jsonPath("$.admins[1].workspaceUserId", is(5)))
                .andExpect(jsonPath("$.admins[1].nickname", is("미소")));
    }
}
