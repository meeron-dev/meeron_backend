package com.cmc.meeron.user.adapter.in;

import com.cmc.meeron.common.exception.CommonErrorCode;
import com.cmc.meeron.common.exception.user.WorkspaceUserNotFoundException;
import com.cmc.meeron.support.restdocs.RestDocsTestSupport;
import com.cmc.meeron.support.security.WithMockJwt;
import com.cmc.meeron.user.adapter.in.request.SetNameRequest;
import com.cmc.meeron.user.application.port.in.response.MeResponseDto;
import com.cmc.meeron.user.application.port.in.response.MyWorkspaceUserResponseDto;
import com.google.common.net.HttpHeaders;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static com.cmc.meeron.config.RestDocsConfig.field;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockJwt
class UserRestControllerTest extends RestDocsTestSupport {

    @DisplayName("회원 정보 가져오기 - 성공")
    @Test
    void get_me_success() throws Exception {

        // given
        MeResponseDto me = createMeResponseDto();
        when(userQueryUseCase.getMe(any()))
                .thenReturn(me);

        // when, then, docs
        mockMvc.perform(get("/api/users/me")
                .header(HttpHeaders.AUTHORIZATION, "Bearer testAccessToken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", is(me.getUserId().intValue())))
                .andExpect(jsonPath("$.loginEmail", is(me.getLoginEmail())))
                .andExpect(jsonPath("$.name", is(me.getName())))
                .andExpect(jsonPath("$.profileImageUrl", is(me.getProfileImageUrl())))
                .andDo(restDocumentationResultHandler.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access Token").attributes(field("constraints", "JWT Access Token With Bearer"))
                        ),
                        responseFields(
                                fieldWithPath("userId").type(JsonFieldType.NUMBER).description("유저 ID"),
                                fieldWithPath("loginEmail").type(JsonFieldType.STRING).description("소셜 로그인 이메일"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("유저 이름"),
                                fieldWithPath("profileImageUrl").type(JsonFieldType.STRING).description("유저 프로필 이미지 URL")
                        )
                ));
    }

    private MeResponseDto createMeResponseDto() {
        return MeResponseDto.builder()
                .userId(1L)
                .loginEmail("test@gmail.com")
                .name("테스트")
                .profileImageUrl("https://test.images.com/12341234")
                .build();
    }

    @DisplayName("회원의 모든 워크스페이스 유저 프로필 가져오기 - 성공")
    @Test
    void get_my_workspace_users_success() throws Exception {

        // given
        List<MyWorkspaceUserResponseDto> myWorkspaceUsers = createWorkspaceUsers();
        when(userQueryUseCase.getMyWorkspaceUsers(any()))
                .thenReturn(myWorkspaceUsers);

        // when, then, docs
        mockMvc.perform(get("/api/users/{userId}/workspace-users", 1L)
                .header(HttpHeaders.AUTHORIZATION, "Bearer testAccessToken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.myWorkspaceUsers", hasSize(2)))
                .andExpect(jsonPath("$.myWorkspaceUsers[0].workspaceUserId", is(myWorkspaceUsers.get(0).getWorkspaceUserId().intValue())))
                .andExpect(jsonPath("$.myWorkspaceUsers[0].workspaceId", is(myWorkspaceUsers.get(0).getWorkspaceId().intValue())))
                .andExpect(jsonPath("$.myWorkspaceUsers[0].workspaceAdmin", is(myWorkspaceUsers.get(0).isWorkspaceAdmin())))
                .andExpect(jsonPath("$.myWorkspaceUsers[0].nickname", is(myWorkspaceUsers.get(0).getNickname())))
                .andExpect(jsonPath("$.myWorkspaceUsers[0].profileImageUrl", is(myWorkspaceUsers.get(0).getProfileImageUrl())))
                .andExpect(jsonPath("$.myWorkspaceUsers[0].position", is(myWorkspaceUsers.get(0).getPosition())))
                .andExpect(jsonPath("$.myWorkspaceUsers[1].workspaceUserId", is(myWorkspaceUsers.get(1).getWorkspaceUserId().intValue())))
                .andExpect(jsonPath("$.myWorkspaceUsers[1].workspaceId", is(myWorkspaceUsers.get(1).getWorkspaceId().intValue())))
                .andExpect(jsonPath("$.myWorkspaceUsers[1].workspaceAdmin", is(myWorkspaceUsers.get(1).isWorkspaceAdmin())))
                .andExpect(jsonPath("$.myWorkspaceUsers[1].nickname", is(myWorkspaceUsers.get(1).getNickname())))
                .andExpect(jsonPath("$.myWorkspaceUsers[1].profileImageUrl", is(myWorkspaceUsers.get(1).getProfileImageUrl())))
                .andExpect(jsonPath("$.myWorkspaceUsers[1].position", is(myWorkspaceUsers.get(1).getPosition())))
                .andDo(restDocumentationResultHandler.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access Token").attributes(field("constraints", "JWT Access Token With Bearer"))
                        ),
                        pathParameters(
                                parameterWithName("userId").description("유저 ID")
                        ),
                        responseFields(
                                fieldWithPath("myWorkspaceUsers[].workspaceUserId").type(JsonFieldType.NUMBER).description("유저의 워크스페이스 유저 ID"),
                                fieldWithPath("myWorkspaceUsers[].workspaceId").type(JsonFieldType.NUMBER).description("워크스페이스 유저가 속한 워크스페이스 ID"),
                                fieldWithPath("myWorkspaceUsers[].workspaceAdmin").type(JsonFieldType.BOOLEAN).description("워크스페이스 유저의 관리자 유무"),
                                fieldWithPath("myWorkspaceUsers[].nickname").type(JsonFieldType.STRING).description("워크스페이스 유저 닉네임"),
                                fieldWithPath("myWorkspaceUsers[].profileImageUrl").type(JsonFieldType.STRING).description("워크스페이스 유저 프로필 이미지 URL"),
                                fieldWithPath("myWorkspaceUsers[].position").type(JsonFieldType.STRING).description("워크스페이스 유저 직책")
                        )
                ));
    }

    private List<MyWorkspaceUserResponseDto> createWorkspaceUsers() {
        return List.of(
                MyWorkspaceUserResponseDto.builder()
                        .workspaceUserId(1L)
                        .workspaceId(1L)
                        .isWorkspaceAdmin(false)
                        .nickname("테스트닉네임1")
                        .profileImageUrl("https://test.images.com/12341234")
                        .position("대리")
                        .build(),
                MyWorkspaceUserResponseDto.builder()
                        .workspaceUserId(2L)
                        .workspaceId(1L)
                        .isWorkspaceAdmin(true)
                        .nickname("테스트닉네임2")
                        .profileImageUrl("https://test.images.com/12341235")
                        .position("매니저")
                        .build()
        );
    }

    @DisplayName("회원의 워크스페이스 유저 중 하나 가져오기 - 성공")
    @Test
    void get_my_workspace_user_success() throws Exception {

        // given
        MyWorkspaceUserResponseDto myWorkspaceUser = createWorkspaceUser();
        when(userQueryUseCase.getMyWorkspaceUser(any()))
                .thenReturn(myWorkspaceUser);

        // when, then, docs
        mockMvc.perform(get("/api/workspace-users/{workspaceUserId}", 1L)
                .header(HttpHeaders.AUTHORIZATION, "Bearer testAccessToken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.workspaceUserId", is(myWorkspaceUser.getWorkspaceUserId().intValue())))
                .andExpect(jsonPath("$.workspaceId", is(myWorkspaceUser.getWorkspaceId().intValue())))
                .andExpect(jsonPath("$.workspaceAdmin", is(myWorkspaceUser.isWorkspaceAdmin())))
                .andExpect(jsonPath("$.nickname", is(myWorkspaceUser.getNickname())))
                .andExpect(jsonPath("$.profileImageUrl", is(myWorkspaceUser.getProfileImageUrl())))
                .andExpect(jsonPath("$.position", is(myWorkspaceUser.getPosition())))
                .andDo(restDocumentationResultHandler.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access Token").attributes(field("constraints", "JWT Access Token With Bearer"))
                        ),
                        pathParameters(
                                parameterWithName("workspaceUserId").description("워크스페이스 유저 ID")
                        ),
                        responseFields(
                                fieldWithPath("workspaceUserId").type(JsonFieldType.NUMBER).description("유저의 워크스페이스 유저 ID"),
                                fieldWithPath("workspaceId").type(JsonFieldType.NUMBER).description("워크스페이스 유저가 속한 워크스페이스 ID"),
                                fieldWithPath("workspaceAdmin").type(JsonFieldType.BOOLEAN).description("워크스페이스 유저의 관리자 유무"),
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("워크스페이스 유저 닉네임"),
                                fieldWithPath("profileImageUrl").type(JsonFieldType.STRING).description("워크스페이스 유저 프로필 이미지 URL"),
                                fieldWithPath("position").type(JsonFieldType.STRING).description("워크스페이스 유저 직책")
                        )
                ));
    }

    private MyWorkspaceUserResponseDto createWorkspaceUser() {
        return MyWorkspaceUserResponseDto.builder()
                .workspaceUserId(1L)
                .workspaceId(1L)
                .isWorkspaceAdmin(true)
                .nickname("테스트닉네임")
                .profileImageUrl("https://test.images.com/12341")
                .position("관리자")
                .build();
    }

    @DisplayName("회원이 지정한 워크스페이스 유저 가져오기 - 실패 / 해당 유저가 없을 경우")
    @Test
    void get_my_workspace_user_fail_workspace_user_not_found() throws Exception {

        // given
        when(userQueryUseCase.getMyWorkspaceUser(any()))
                .thenThrow(new WorkspaceUserNotFoundException());

        // when, then, docs
        mockMvc.perform(get("/api/workspace-users/{workspaceUserId}", 1L)
                .header(HttpHeaders.AUTHORIZATION, "Bearer testAccessToken"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.code", is(CommonErrorCode.APPLICATION_EXCEPTION.getCode())));
    }

    @DisplayName("워크스페이스 유저 검색 - 성공")
    @Test
    void search_workspace_users_success() throws Exception {

        // given
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("workspaceId", "1");
        params.add("nickname", "테스트");
        List<MyWorkspaceUserResponseDto> workspaceUsers = createWorkspaceUsers();
        when(userQueryUseCase.searchWorkspaceUsers(any()))
                .thenReturn(workspaceUsers);

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/workspace-users")
                .header(HttpHeaders.AUTHORIZATION, "Bearer testAccessToken")
                .params(params)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.workspaceUsers", hasSize(2)))
                .andExpect(jsonPath("$.workspaceUsers[0].workspaceUserId", is(workspaceUsers.get(0).getWorkspaceUserId().intValue())))
                .andExpect(jsonPath("$.workspaceUsers[0].profileImageUrl", is(workspaceUsers.get(0).getProfileImageUrl())))
                .andExpect(jsonPath("$.workspaceUsers[0].nickname", is(workspaceUsers.get(0).getNickname())))
                .andExpect(jsonPath("$.workspaceUsers[0].position", is(workspaceUsers.get(0).getPosition())))
                .andExpect(jsonPath("$.workspaceUsers[0].workspaceAdmin", is(workspaceUsers.get(0).isWorkspaceAdmin())))
                .andExpect(jsonPath("$.workspaceUsers[0].workspaceId", is(workspaceUsers.get(0).getWorkspaceId().intValue())))
                .andExpect(jsonPath("$.workspaceUsers[1].workspaceUserId", is(workspaceUsers.get(1).getWorkspaceUserId().intValue())))
                .andExpect(jsonPath("$.workspaceUsers[1].profileImageUrl", is(workspaceUsers.get(1).getProfileImageUrl())))
                .andExpect(jsonPath("$.workspaceUsers[1].nickname", is(workspaceUsers.get(1).getNickname())))
                .andExpect(jsonPath("$.workspaceUsers[1].position", is(workspaceUsers.get(1).getPosition())))
                .andExpect(jsonPath("$.workspaceUsers[1].workspaceAdmin", is(workspaceUsers.get(1).isWorkspaceAdmin())))
                .andExpect(jsonPath("$.workspaceUsers[1].workspaceId", is(workspaceUsers.get(1).getWorkspaceId().intValue())))
                .andDo(restDocumentationResultHandler.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access Token").attributes(field("constraints", "JWT Access Token With Bearer"))
                        ),
                        requestParameters(
                                parameterWithName("workspaceId").description("워크스페이스 ID"),
                                parameterWithName("nickname").description("찾을 워크스페이스 유저 닉네임")
                        ),
                        responseFields(
                                fieldWithPath("workspaceUsers[].workspaceUserId").type(JsonFieldType.NUMBER).description("워크스페이스 유저 ID"),
                                fieldWithPath("workspaceUsers[].profileImageUrl").type(JsonFieldType.STRING).optional().description("워크스페이스 유저 프로필 이미지 URL"),
                                fieldWithPath("workspaceUsers[].nickname").type(JsonFieldType.STRING).description("찾는 워크스페이스 유저 닉네임"),
                                fieldWithPath("workspaceUsers[].position").type(JsonFieldType.STRING).description("찾는 워크스페이스 유저 직책"),
                                fieldWithPath("workspaceUsers[].workspaceId").type(JsonFieldType.NUMBER).description("워크스페이스 유저가 속한 워크스페이스 ID"),
                                fieldWithPath("workspaceUsers[].workspaceAdmin").type(JsonFieldType.BOOLEAN).description("워크스페이스 유저의 관리자 유무")
                        )
                ));
    }

    @DisplayName("워크스페이스 유저 실패 - 워크스페이스 ID, 검색할 워크스페이스 유저 닉네임을 주지 않을 경우")
    @Test
    void search_workspace_users_fail_require_workspace_id_nickname() throws Exception {

        // given, when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/workspace-users")
                .header(HttpHeaders.AUTHORIZATION, "Bearer testAccessToken")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(2)))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.code", is(CommonErrorCode.BIND_EXCEPTION.getCode())));
    }

    @DisplayName("팀에 속한 모든 워크스페이스 유저 정보 조회 - 성공")
    @Test
    void get_team_members_success() throws Exception {

        // given
        List<MyWorkspaceUserResponseDto> workspaceUsers = createWorkspaceUsers();
        when(userQueryUseCase.getTeamUsers(any()))
                .thenReturn(workspaceUsers);

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/teams/{teamId}/workspace-users", "1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer testAccessToken")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.workspaceUsers", hasSize(2)))
                .andExpect(jsonPath("$.workspaceUsers[0].workspaceUserId", is(workspaceUsers.get(0).getWorkspaceUserId().intValue())))
                .andExpect(jsonPath("$.workspaceUsers[0].profileImageUrl", is(workspaceUsers.get(0).getProfileImageUrl())))
                .andExpect(jsonPath("$.workspaceUsers[0].nickname", is(workspaceUsers.get(0).getNickname())))
                .andExpect(jsonPath("$.workspaceUsers[0].position", is(workspaceUsers.get(0).getPosition())))
                .andExpect(jsonPath("$.workspaceUsers[0].workspaceAdmin", is(workspaceUsers.get(0).isWorkspaceAdmin())))
                .andExpect(jsonPath("$.workspaceUsers[0].workspaceId", is(workspaceUsers.get(0).getWorkspaceId().intValue())))
                .andExpect(jsonPath("$.workspaceUsers[1].workspaceUserId", is(workspaceUsers.get(1).getWorkspaceUserId().intValue())))
                .andExpect(jsonPath("$.workspaceUsers[1].profileImageUrl", is(workspaceUsers.get(1).getProfileImageUrl())))
                .andExpect(jsonPath("$.workspaceUsers[1].nickname", is(workspaceUsers.get(1).getNickname())))
                .andExpect(jsonPath("$.workspaceUsers[1].position", is(workspaceUsers.get(1).getPosition())))
                .andExpect(jsonPath("$.workspaceUsers[1].workspaceAdmin", is(workspaceUsers.get(1).isWorkspaceAdmin())))
                .andExpect(jsonPath("$.workspaceUsers[1].workspaceId", is(workspaceUsers.get(1).getWorkspaceId().intValue())))
                .andDo(restDocumentationResultHandler.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access Token").attributes(field("constraints", "JWT Access Token With Bearer"))
                        ),
                        pathParameters(
                                parameterWithName("teamId").description("팀원들을 찾을 팀 ID")
                        ),
                        responseFields(
                                fieldWithPath("workspaceUsers[].workspaceUserId").type(JsonFieldType.NUMBER).description("워크스페이스 유저 ID"),
                                fieldWithPath("workspaceUsers[].profileImageUrl").type(JsonFieldType.STRING).optional().description("워크스페이스 유저 프로필 이미지 URL"),
                                fieldWithPath("workspaceUsers[].nickname").type(JsonFieldType.STRING).description("찾는 워크스페이스 유저 닉네임"),
                                fieldWithPath("workspaceUsers[].position").type(JsonFieldType.STRING).description("찾는 워크스페이스 유저 직책"),
                                fieldWithPath("workspaceUsers[].workspaceId").type(JsonFieldType.NUMBER).description("워크스페이스 유저가 속한 워크스페이스 ID"),
                                fieldWithPath("workspaceUsers[].workspaceAdmin").type(JsonFieldType.BOOLEAN).description("워크스페이스 유저의 관리자 유무")
                        )
                ));
    }

    @DisplayName("유저의 성함 등록 - 성공")
    @Test
    void set_user_name_success() throws Exception {

        // given
        SetNameRequest request = createSetNameRequest();

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/users/name")
                .header(HttpHeaders.AUTHORIZATION, "Bearer testAccessToken")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(restDocumentationResultHandler.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access Token").attributes(field("constraints", "JWT Access Token With Bearer"))
                        ),
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("성함").attributes(field("constraints", "1자 이상 5자 이하"))
                        )
                ));
    }

    @DisplayName("유저의 성함 실패 - 성함을 주지 않을 경우")
    @Test
    void set_user_name_fail_name_not_blank() throws Exception {

        // given
        SetNameRequest request = SetNameRequest.builder()
                .name("    ")
                .build();

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/users/name")
                .header(HttpHeaders.AUTHORIZATION, "Bearer testAccessToken")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.code", is(CommonErrorCode.BIND_EXCEPTION.getCode())))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())));
    }

    @DisplayName("유저의 성함 실패 - 5자를 초과할 경우")
    @Test
    void set_user_name_fail_name_max_five_words() throws Exception {

        // given
        SetNameRequest request = SetNameRequest.builder()
                .name("다섯글자를넘었어")
                .build();

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/users/name")
                .header(HttpHeaders.AUTHORIZATION, "Bearer testAccessToken")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.code", is(CommonErrorCode.BIND_EXCEPTION.getCode())))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())));
    }

    private SetNameRequest createSetNameRequest() {
        return SetNameRequest.builder()
                .name("테스트")
                .build();
    }
}
