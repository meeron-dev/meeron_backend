package com.cmc.meeron.user.integration;

import com.cmc.meeron.auth.application.port.in.AuthUseCase;
import com.cmc.meeron.auth.application.port.in.request.LoginRequestDto;
import com.cmc.meeron.auth.application.port.in.response.TokenResponseDto;
import com.cmc.meeron.support.IntegrationTest;
import com.cmc.meeron.support.security.WithMockJwt;
import com.cmc.meeron.user.adapter.in.response.WorkspaceUserResponse;
import com.google.common.net.HttpHeaders;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.stream.Stream;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserIntegrationTest extends IntegrationTest {

    @Autowired
    AuthUseCase authUseCase;

    @DisplayName("내 정보 조회 - 성공")
    @Test
    void get_me_success() throws Exception {

        // given
        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
                .email("test1@test.com")
                .provider("KAKAO")
                .build();
        TokenResponseDto token = authUseCase.login(loginRequestDto);

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/me")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", is(1)))
                .andExpect(jsonPath("$.loginEmail", is(loginRequestDto.getEmail())));
    }

    @WithMockJwt
    @DisplayName("유저의 워크스페이스 유저 조회 - 성공")
    @Test
    void get_workspace_users_success() throws Exception {

        // given, when, then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/{userId}/workspace-users", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.myWorkspaceUsers", hasSize(1)));
    }

    @WithMockJwt
    @DisplayName("워크스페이스 내 유저 조회 - 성공")
    @ParameterizedTest
    @MethodSource("searchWorkspaceUserParameters")
    void search_workspace_users_success(MultiValueMap<String, String> params,
                                        int expectedValue,
                                        WorkspaceUserResponse.WorkspaceUser response) throws Exception {

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/workspace-users")
                .contentType(MediaType.APPLICATION_JSON)
                .params(params))
                .andExpect(status().isOk())
                .andExpect(jsonPath("workspaceUsers", hasSize(expectedValue)))
                .andExpect(jsonPath("workspaceUsers[0].workspaceUserId", is(response.getWorkspaceUserId().intValue())))
                .andExpect(jsonPath("workspaceUsers[0].nickname", is(response.getNickname())))
                .andExpect(jsonPath("workspaceUsers[0].position", is(response.getPosition())));
    }

    private static Stream<Arguments> searchWorkspaceUserParameters() {
        MultiValueMap<String, String> params1 = new LinkedMultiValueMap<>();
        params1.add("nickname", "무");
        params1.add("workspaceId", "1");
        WorkspaceUserResponse.WorkspaceUser response1 = WorkspaceUserResponse.WorkspaceUser.builder()
                .workspaceUserId(1L)
                .nickname("무무")
                .position("Server / PM")
                .build();
        MultiValueMap<String, String> params2 = new LinkedMultiValueMap<>();
        params2.add("nickname", "제");
        params2.add("workspaceId", "1");
        WorkspaceUserResponse.WorkspaceUser response2 = WorkspaceUserResponse.WorkspaceUser.builder()
                .workspaceUserId(3L)
                .nickname("제로")
                .position("Android")
                .build();
        return Stream.of(
                Arguments.of(params1, 1, response1),
                Arguments.of(params2, 1, response2)
        );
    }

    @WithMockJwt
    @DisplayName("워크스페이스 유저 상세 조회 - 성공")
    @Test
    void get_workspace_user_success() throws Exception {

        // given, when, then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/workspace-users/{workspaceUserId}", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.workspaceUserId", is(1)))
                .andExpect(jsonPath("$.workspaceId", is(1)))
                .andExpect(jsonPath("$.workspaceAdmin", is(false)))
                .andExpect(jsonPath("$.nickname", is("무무")))
                .andExpect(jsonPath("$.profileImageUrl", nullValue()))
                .andExpect(jsonPath("$.position", is("Server / PM")));
    }

    @WithMockJwt
    @DisplayName("팀에 속한 워크스페이스 유저 조회 - 성공")
    @ParameterizedTest
    @MethodSource("requestTeamWorkspaceUsers")
    void get_team_users_success(Long teamId, int expectedValue) throws Exception {

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/teams/{teamId}/workspace-users", teamId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.workspaceUsers", hasSize(expectedValue)));
    }

    private static Stream<Arguments> requestTeamWorkspaceUsers() {
        return Stream.of(
                Arguments.of(1L, 1),
                Arguments.of(2L, 1),
                Arguments.of(3L, 3)
        );
    }
}
