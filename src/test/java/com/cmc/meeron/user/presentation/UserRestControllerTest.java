package com.cmc.meeron.user.presentation;

import com.cmc.meeron.common.exception.user.WorkspaceUserNotFoundException;
import com.cmc.meeron.support.restdocs.RestDocsTestSupport;
import com.cmc.meeron.support.security.WithMockJwt;
import com.cmc.meeron.user.application.dto.response.MeResponseDto;
import com.cmc.meeron.user.application.dto.response.MyWorkspaceUserResponseDto;
import com.google.common.net.HttpHeaders;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.List;

import static com.cmc.meeron.config.RestDocsConfig.field;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
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
                .andExpect(jsonPath("$.contactEmail", is(me.getContactEmail())))
                .andExpect(jsonPath("$.name", is(me.getName())))
                .andExpect(jsonPath("$.profileImageUrl", is(me.getProfileImageUrl())))
                .andExpect(jsonPath("$.phone", is(me.getPhone())))
                .andDo(restDocumentationResultHandler.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access Token").attributes(field("constraints", "JWT Access Token With Bearer"))
                        ),
                        responseFields(
                                fieldWithPath("userId").type(JsonFieldType.NUMBER).description("유저 ID"),
                                fieldWithPath("loginEmail").type(JsonFieldType.STRING).description("소셜 로그인 이메일"),
                                fieldWithPath("contactEmail").type(JsonFieldType.STRING).description("연락 메일"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("유저 이름"),
                                fieldWithPath("profileImageUrl").type(JsonFieldType.STRING).description("유저 프로필 이미지 URL"),
                                fieldWithPath("phone").type(JsonFieldType.STRING).description("유저 휴대전화번호")
                        )
                ));
    }

    private MeResponseDto createMeResponseDto() {
        return MeResponseDto.builder()
                .userId(1L)
                .loginEmail("test@gmail.com")
                .contactEmail("test@naver.com")
                .name("테스트")
                .profileImageUrl("https://test.images.com/12341234")
                .phone("01023412341")
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
                .andExpect(jsonPath("$.code", is("MEERON-400")));
    }
}
