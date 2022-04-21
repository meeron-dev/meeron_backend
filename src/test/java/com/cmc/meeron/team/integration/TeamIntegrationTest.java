package com.cmc.meeron.team.integration;

import com.cmc.meeron.support.IntegrationTest;
import com.cmc.meeron.support.security.WithMockJwt;
import com.cmc.meeron.team.adapter.in.request.*;
import com.cmc.meeron.team.application.port.out.TeamQueryPort;
import com.cmc.meeron.team.domain.Team;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.stream.Stream;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockJwt
public class TeamIntegrationTest extends IntegrationTest {

    @Autowired TeamQueryPort teamQueryPort;

    @DisplayName("워크스페이스 내 팀 조회 - 성공")
    @ParameterizedTest
    @MethodSource("workspaceTeamsParameters")
    void get_workspace_teams_success(MultiValueMap<String, String> params, int expectedValue) throws Exception {

        // given, when, then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/teams")
                .contentType(MediaType.APPLICATION_JSON)
                .params(params))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.teams", hasSize(expectedValue)));
    }

    private static Stream<Arguments> workspaceTeamsParameters() {
        MultiValueMap<String, String> params1 = new LinkedMultiValueMap<>();
        params1.add("workspaceId", "1");
        MultiValueMap<String, String> params2 = new LinkedMultiValueMap<>();
        params2.add("workspaceId", "2");
        return Stream.of(
                Arguments.of(params1, 3),
                Arguments.of(params2, 1)
        );
    }

    @DisplayName("팀 생성 - 성공")
    @Test
    void create_team_success() throws Exception {

        // given
        CreateTeamRequest request = createCreateTeamRequest();

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/teams")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private CreateTeamRequest createCreateTeamRequest() {
        return CreateTeamRequest.builder()
                .workspaceId(1L)
                .teamName("테스트 팀")
                .build();
    }

    @Sql("classpath:team-test.sql")
    @DisplayName("팀 생성 - 실패 / 워크스페이스에 팀이 5팀 이상일 경우")
    @Test
    void create_team_fail_over_five_teams() throws Exception {

        // given
        CreateTeamRequest request = createCreateTeamRequest();

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/teams")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Sql("classpath:team-test.sql")
    @DisplayName("팀 삭제 - 실패 / 관리자가 아닌 경우")
    @Test
    void delete_team_fail_not_admin() throws Exception {

        // given
        DeleteTeamRequest request = DeleteTeamRequestBuilder.build();

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/teams/{teamId}", "5")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Sql("classpath:team-test.sql")
    @DisplayName("팀 삭제 - 성공")
    @Test
    void delete_team_success() throws Exception {

        // given
        DeleteTeamRequest request = DeleteTeamRequestBuilder.buildIntegrationSuccessCase();

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/teams/{teamId}", "5")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Sql("classpath:team-test.sql")
    @DisplayName("팀명 변경 - 실패 / 관리자 권한이 없을 경우")
    @Test
    void modify_team_name_fail_not_admin() throws Exception {

        // given
        ModifyTeamNameRequest request = ModifyTeamNameRequestBuilder.build();

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/teams/{teamId}/name", "6")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Sql("classpath:team-test.sql")
    @DisplayName("팀명 변경 - 성공")
    @Test
    void modify_team_name_success() throws Exception {

        // given
        ModifyTeamNameRequest request = ModifyTeamNameRequestBuilder.buildIntegrationSuccessCase();

        // when
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/teams/{teamId}/name", "6")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        flushAndClear();

        // then
        Team team = teamQueryPort.findById(6L).orElseThrow();
        assertEquals(request.getTeamName(), team.getName());
    }

    @DisplayName("회의를 주관하는 팀 조회 - 성공")
    @Test
    void get_meeting_host_team_success() throws Exception {

        // given, when, then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/meetings/{meetingId}/host-team", "3")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.teamId", is(2)))
                .andExpect(jsonPath("$.teamName", is("기획팀")));
    }
}
