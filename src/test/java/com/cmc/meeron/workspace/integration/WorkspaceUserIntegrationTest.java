package com.cmc.meeron.workspace.integration;

import com.cmc.meeron.common.exception.workspace.WorkspaceUserErrorCode;
import com.cmc.meeron.support.IntegrationTest;
import com.cmc.meeron.support.security.WithMockJwt;
import com.cmc.meeron.user.adapter.in.request.FindWorkspaceUserRequestBuilder;
import com.cmc.meeron.workspace.adapter.in.request.*;
import com.cmc.meeron.workspace.adapter.in.response.WorkspaceUserResponse;
import com.cmc.meeron.workspace.application.port.in.request.FindNoneTeamWorkspaceUsersParametersBuilder;
import com.cmc.meeron.workspace.application.port.out.WorkspaceUserQueryPort;
import com.cmc.meeron.workspace.domain.WorkspaceUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.stream.Stream;

import static com.cmc.meeron.file.FileFixture.FILE;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockJwt
class WorkspaceUserIntegrationTest extends IntegrationTest {

    @Autowired WorkspaceUserQueryPort workspaceUserQueryPort;

    @DisplayName("유저의 워크스페이스 유저 조회 - 성공")
    @Test
    void get_workspace_users_success() throws Exception {

        // given, when, then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/{userId}/workspace-users", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.myWorkspaceUsers", hasSize(1)));
    }

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
                .andExpect(jsonPath("$.profileImageUrl", blankString()))
                .andExpect(jsonPath("$.position", is("Server / PM")));
    }

    @DisplayName("워크스페이스 내 유저 조회 - 성공")
    @ParameterizedTest
    @MethodSource("searchWorkspaceUserParameters")
    void search_workspace_users_success(MultiValueMap<String, String> params,
                                        int expectedValue,
                                        WorkspaceUserResponse response) throws Exception {

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/workspace-users")
                .contentType(MediaType.APPLICATION_JSON)
                .params(params))
                .andExpect(status().isOk())
                .andExpect(jsonPath("workspaceUsers", hasSize(expectedValue)))
                .andExpect(jsonPath("workspaceUsers[0].workspaceUserId", is(response.getWorkspaceUserId().intValue())))
                .andExpect(jsonPath("workspaceUsers[0].nickname", is(response.getNickname())))
                .andExpect(jsonPath("workspaceUsers[0].position", is(response.getPosition())))
                .andExpect(jsonPath("workspaceUsers[0].workspaceAdmin", is(response.isWorkspaceAdmin())))
                .andExpect(jsonPath("workspaceUsers[0].workspaceId", is(response.getWorkspaceId().intValue())))
                .andExpect(jsonPath("workspaceUsers[0].profileImageUrl", is(response.getProfileImageUrl())));
    }

    private static Stream<Arguments> searchWorkspaceUserParameters() {
        MultiValueMap<String, String> params1 = new LinkedMultiValueMap<>();
        params1.add("nickname", "무");
        params1.add("workspaceId", "1");
        WorkspaceUserResponse response1 = WorkspaceUserResponse.builder()
                .workspaceUserId(1L)
                .workspaceId(1L)
                .isWorkspaceAdmin(false)
                .nickname("무무")
                .profileImageUrl("")
                .position("Server / PM")
                .build();
        MultiValueMap<String, String> params2 = new LinkedMultiValueMap<>();
        params2.add("nickname", "제");
        params2.add("workspaceId", "1");
        WorkspaceUserResponse response2 = WorkspaceUserResponse.builder()
                .workspaceUserId(3L)
                .workspaceId(1L)
                .isWorkspaceAdmin(false)
                .nickname("제로")
                .profileImageUrl("")
                .position("Android")
                .build();
        return Stream.of(
                Arguments.of(params1, 1, response1),
                Arguments.of(params2, 1, response2)
        );
    }

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

    @DisplayName("워크스페이스 유저 관리자 생성 - 성공")
    @Test
    void create_workspace_user_admin_success() throws Exception {

        // given
        CreateWorkspaceUserRequest request = createCreateWorkspaceUserRequest();
        MockMultipartFile profile = FILE;

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/workspace-users/admin")
                .file(profile)
                .file(createJsonFile(request))
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.workspaceUserId", notNullValue()))
                .andExpect(jsonPath("$.nickname", is(request.getNickname())))
                .andExpect(jsonPath("$.workspaceAdmin", is(true)))
                .andExpect(jsonPath("$.position", is(request.getPosition())))
                .andExpect(jsonPath("$.profileImageUrl", notNullValue()))
                .andExpect(jsonPath("$.email", is(request.getEmail())))
                .andExpect(jsonPath("$.phone", is(request.getPhone())));
    }

    private CreateWorkspaceUserRequest createCreateWorkspaceUserRequest() {
        return CreateWorkspaceUserRequest.builder()
                .workspaceId(1L)
                .nickname("테스트")
                .position("개발자")
                .email("test@test.com")
                .phone("010-1234-1234")
                .build();
    }

    @DisplayName("워크스페이스 유저 생성 - 성공")
    @Test
    void create_workspace_user_success() throws Exception {

        // given
        CreateWorkspaceUserRequest request = createCreateWorkspaceUserRequest();
        MockMultipartFile profile = FILE;

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/workspace-users")
                .file(profile)
                .file(createJsonFile(request))
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.workspaceUserId", notNullValue()))
                .andExpect(jsonPath("$.nickname", is(request.getNickname())))
                .andExpect(jsonPath("$.workspaceAdmin", is(false)))
                .andExpect(jsonPath("$.position", is(request.getPosition())))
                .andExpect(jsonPath("$.profileImageUrl", notNullValue()))
                .andExpect(jsonPath("$.email", is(request.getEmail())))
                .andExpect(jsonPath("$.phone", is(request.getPhone())));
    }

    @DisplayName("닉네임 중복 체크 - 성공 / 중복된 닉네임이 없을 경우")
    @Test
    void check_duplicate_nickname_success_not_duplicate() throws Exception {

        // given
        FindWorkspaceUserRequest request = FindWorkspaceUserRequestBuilder.build();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("workspaceId", request.getWorkspaceId().toString());
        params.add("nickname", request.getNickname());

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/workspace-users/nickname")
                .params(params)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.duplicate", is(false)));
    }

    @DisplayName("닉네임 중복 체크 - 실패 / 중복된 닉네임이 있을 경우")
    @Test
    void check_duplicate_nickname_success_duplicate() throws Exception {

        // given
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("workspaceId", "1");
        params.add("nickname", "무무");

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/workspace-users/nickname")
                .params(params)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.code", is(WorkspaceUserErrorCode.DUPLICATE_NICKNAME.getCode())));
    }

    @Sql("classpath:workspace-user-test.sql")
    @DisplayName("팀에 속하지 않은 팀원 조회 - 성공")
    @Test
    void get_none_team_users_success() throws Exception {

        // given
        FindNoneTeamWorkspaceUsersParameters parameters = FindNoneTeamWorkspaceUsersParametersBuilder.build();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("workspaceId", parameters.getWorkspaceId().toString());

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/teams/none/workspace-users")
                .params(params)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.workspaceUsers", hasSize(4)));
    }

    @Sql("classpath:workspace-user-test.sql")
    @DisplayName("팀원 추가 - 성공")
    @Test
    void join_team_users_success() throws Exception {

        // given
        JoinTeamUsersRequest request = JoinTeamUsersRequestBuilder.buildIntegrationSuccessCase();

        // when
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/teams/{teamId}/workspace-users", "3")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // then
        flushAndClear();
        List<WorkspaceUser> noneTeamUsers = workspaceUserQueryPort.findByWorkspaceIdAndTeamIsNull(1L);
        assertEquals(2, noneTeamUsers.size());
        List<WorkspaceUser> devTeamUsers = workspaceUserQueryPort.findByTeamId(3L);
        assertEquals(6, devTeamUsers.size());
    }

    @Sql("classpath:workspace-user-test.sql")
    @DisplayName("팀원 추가 - 실패 / 관리자가 아닌 경우")
    @Test
    void join_team_users_fail_not_admin() throws Exception {

        // given
        JoinTeamUsersRequest request = JoinTeamUsersRequestBuilder.build();

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/teams/{teamId}/workspace-users", "3")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Sql("classpath:workspace-user-test.sql")
    @DisplayName("팀원 추방 - 실패 / 관리자가 아닌 경우")
    @Test
    void kick_out_team_user_fail_not_admin() throws Exception {

        // given
        KickOutTeamUserRequest request = KickOutTeamUserRequestBuilder.build();

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/workspace-users/{workspaceUserId}/team", "8")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Sql("classpath:workspace-user-test.sql")
    @DisplayName("팀원 추방 - 성공")
    @Test
    void kick_out_team_success() throws Exception {

        // given
        KickOutTeamUserRequest request = KickOutTeamUserRequestBuilder.buildIntegrationSuccessCase();

        // when
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/workspace-users/{workspaceUserId}/team", "10")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // then
        flushAndClear();
        List<WorkspaceUser> workspaceUsers = workspaceUserQueryPort.findByTeamId(3L);
        assertEquals(3, workspaceUsers.size());
    }
}
