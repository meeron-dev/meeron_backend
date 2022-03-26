package com.cmc.meeron.workspace.adapter.in;

import com.cmc.meeron.common.exception.CommonErrorCode;
import com.cmc.meeron.common.exception.user.NicknameDuplicateException;
import com.cmc.meeron.common.exception.user.UserNotFoundException;
import com.cmc.meeron.common.exception.user.WorkspaceUserNotFoundException;
import com.cmc.meeron.common.exception.workspace.WorkspaceNotFoundException;
import com.cmc.meeron.support.restdocs.RestDocsTestSupport;
import com.cmc.meeron.support.security.WithMockJwt;
import com.cmc.meeron.workspace.adapter.in.request.CreateWorkspaceUserRequest;
import com.cmc.meeron.workspace.adapter.in.request.FindWorkspaceUserRequest;
import com.cmc.meeron.user.adapter.in.request.FindWorkspaceUserRequestBuilder;
import com.cmc.meeron.workspace.application.port.in.response.MyWorkspaceUserResponseDto;
import com.cmc.meeron.workspace.application.port.in.response.WorkspaceUserResponseDto;
import com.google.common.net.HttpHeaders;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static com.cmc.meeron.config.RestDocsConfig.field;
import static com.cmc.meeron.file.FileFixture.FILE;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockJwt
class WorkspaceUserRestControllerTest extends RestDocsTestSupport {


    @DisplayName("회원의 모든 워크스페이스 유저 프로필 가져오기 - 성공")
    @Test
    void get_my_workspace_users_success() throws Exception {

        // given
        List<MyWorkspaceUserResponseDto> myWorkspaceUsers = createWorkspaceUsers();
        when(workspaceUserQueryUseCase.getMyWorkspaceUsers(any()))
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
                .andExpect(jsonPath("$.myWorkspaceUsers[0].email", is(myWorkspaceUsers.get(0).getEmail())))
                .andExpect(jsonPath("$.myWorkspaceUsers[1].workspaceUserId", is(myWorkspaceUsers.get(1).getWorkspaceUserId().intValue())))
                .andExpect(jsonPath("$.myWorkspaceUsers[1].workspaceId", is(myWorkspaceUsers.get(1).getWorkspaceId().intValue())))
                .andExpect(jsonPath("$.myWorkspaceUsers[1].workspaceAdmin", is(myWorkspaceUsers.get(1).isWorkspaceAdmin())))
                .andExpect(jsonPath("$.myWorkspaceUsers[1].nickname", is(myWorkspaceUsers.get(1).getNickname())))
                .andExpect(jsonPath("$.myWorkspaceUsers[1].profileImageUrl", is(myWorkspaceUsers.get(1).getProfileImageUrl())))
                .andExpect(jsonPath("$.myWorkspaceUsers[1].position", is(myWorkspaceUsers.get(1).getPosition())))
                .andExpect(jsonPath("$.myWorkspaceUsers[1].email", is(myWorkspaceUsers.get(1).getEmail())))
                .andDo(restDocumentationResultHandler.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access Token").attributes(field("constraints", "JWT Access Token With Bearer"))
                        ),
                        pathParameters(
                                parameterWithName("userId").description("유저 ID")
                        ),
                        responseFields(
                                fieldWithPath("myWorkspaceUsers[].workspaceUserId").type(JsonFieldType.NUMBER).description("워크스페이스 유저 ID"),
                                fieldWithPath("myWorkspaceUsers[].profileImageUrl").type(JsonFieldType.STRING).optional().description("워크스페이스 유저 프로필 이미지 URL, 없을 경우 \"\" 반환"),
                                fieldWithPath("myWorkspaceUsers[].nickname").type(JsonFieldType.STRING).description("찾는 워크스페이스 유저 닉네임"),
                                fieldWithPath("myWorkspaceUsers[].position").type(JsonFieldType.STRING).description("찾는 워크스페이스 유저 직책, 없을 경우 \"\" 반환"),
                                fieldWithPath("myWorkspaceUsers[].workspaceId").type(JsonFieldType.NUMBER).description("워크스페이스 유저가 속한 워크스페이스 ID"),
                                fieldWithPath("myWorkspaceUsers[].workspaceAdmin").type(JsonFieldType.BOOLEAN).description("워크스페이스 유저의 관리자 유무"),
                                fieldWithPath("myWorkspaceUsers[].email").type(JsonFieldType.STRING).description("워크스페이스 유저 이메일, 없을 경우 \"\" 반환")
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
                        .email("test@test.com")
                        .build(),
                MyWorkspaceUserResponseDto.builder()
                        .workspaceUserId(2L)
                        .workspaceId(1L)
                        .isWorkspaceAdmin(true)
                        .nickname("테스트닉네임2")
                        .profileImageUrl("https://test.images.com/12341235")
                        .position("매니저")
                        .email("test2@test.com")
                        .build()
        );
    }

    @DisplayName("회원의 워크스페이스 유저 중 하나 가져오기 - 성공")
    @Test
    void get_my_workspace_user_success() throws Exception {

        // given
        MyWorkspaceUserResponseDto myWorkspaceUser = createWorkspaceUser();
        when(workspaceUserQueryUseCase.getMyWorkspaceUser(any()))
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
                                fieldWithPath("profileImageUrl").type(JsonFieldType.STRING).description("워크스페이스 유저 프로필 이미지 URL, 없을 경우 \"\" 반환"),
                                fieldWithPath("position").type(JsonFieldType.STRING).description("워크스페이스 유저 직책, 없을 경우 \"\" 반환"),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("워크스페이스 유저 이메일, 없을 경우 \"\" 반환")
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
                .email("test@test.com")
                .build();
    }

    @DisplayName("회원이 지정한 워크스페이스 유저 가져오기 - 실패 / 해당 유저가 없을 경우")
    @Test
    void get_my_workspace_user_fail_workspace_user_not_found() throws Exception {

        // given
        when(workspaceUserQueryUseCase.getMyWorkspaceUser(any()))
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
        when(workspaceUserQueryUseCase.searchWorkspaceUsers(any()))
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
                                fieldWithPath("workspaceUsers[].profileImageUrl").type(JsonFieldType.STRING).optional().description("워크스페이스 유저 프로필 이미지 URL, 없을 경우 \"\" 반환"),
                                fieldWithPath("workspaceUsers[].nickname").type(JsonFieldType.STRING).description("찾는 워크스페이스 유저 닉네임"),
                                fieldWithPath("workspaceUsers[].position").type(JsonFieldType.STRING).description("찾는 워크스페이스 유저 직책, 없을 경우 \"\" 반환"),
                                fieldWithPath("workspaceUsers[].workspaceId").type(JsonFieldType.NUMBER).description("워크스페이스 유저가 속한 워크스페이스 ID"),
                                fieldWithPath("workspaceUsers[].workspaceAdmin").type(JsonFieldType.BOOLEAN).description("워크스페이스 유저의 관리자 유무"),
                                fieldWithPath("workspaceUsers[].email").type(JsonFieldType.STRING).description("워크스페이스 유저 이메일, 없을 경우 \"\" 반환")
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
        when(workspaceUserQueryUseCase.getTeamUsers(any()))
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
                                fieldWithPath("workspaceUsers[].profileImageUrl").type(JsonFieldType.STRING).optional().description("워크스페이스 유저 프로필 이미지 URL, 없을 경우 \"\" 반환"),
                                fieldWithPath("workspaceUsers[].nickname").type(JsonFieldType.STRING).description("찾는 워크스페이스 유저 닉네임"),
                                fieldWithPath("workspaceUsers[].position").type(JsonFieldType.STRING).description("찾는 워크스페이스 유저 직책, 없을 경우 \"\" 반환"),
                                fieldWithPath("workspaceUsers[].workspaceId").type(JsonFieldType.NUMBER).description("워크스페이스 유저가 속한 워크스페이스 ID"),
                                fieldWithPath("workspaceUsers[].workspaceAdmin").type(JsonFieldType.BOOLEAN).description("워크스페이스 유저의 관리자 유무"),
                                fieldWithPath("workspaceUsers[].email").type(JsonFieldType.STRING).description("워크스페이스 유저 이메일, 없을 경우 \"\" 반환")
                        )
                ));
    }
    @DisplayName("워크스페이스 유저 관리자 생성 - 실패 / 제약조건을 지키지 않을 경우")
    @Test
    void create_workspace_user_fail_not_valid() throws Exception {

        // given
        CreateWorkspaceUserRequest request = CreateWorkspaceUserRequest.builder()
                .nickname("      ")
                .position("      ")
                .build();

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.multipart("/api/workspace-users/admin")
                .file(createJsonFile(request))
                .header(HttpHeaders.AUTHORIZATION, "Bearer testAccessToken")
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.code", is(CommonErrorCode.BIND_EXCEPTION.getCode())))
                .andExpect(jsonPath("$.errors", hasSize(3)));
    }

    @DisplayName("워크스페이스 유저 관리자  생성 - 실패 / 닉네임이 중복된 경우")
    @Test
    void create_workspace_user_fail_duplicate_nickname() throws Exception {

        // given
        CreateWorkspaceUserRequest request = createCreateWorkspaceUserRequest();
        MockMultipartFile profile = FILE;
        when(workspaceUserCommandUseCase.createWorkspaceUser(any()))
                .thenThrow(new NicknameDuplicateException());

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.multipart("/api/workspace-users/admin")
                .file(profile)
                .file(createJsonFile(request))
                .header(HttpHeaders.AUTHORIZATION, "Bearer testAccessToken")
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.code", is(CommonErrorCode.APPLICATION_EXCEPTION.getCode())));
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

    @DisplayName("워크스페이스 유저 관리자 생성 - 실패 / 워크스페이스가 없을 경우")
    @Test
    void create_workspace_user_fail_not_found_workspace() throws Exception {

        // given
        CreateWorkspaceUserRequest request = createCreateWorkspaceUserRequest();
        MockMultipartFile profile = FILE;
        when(workspaceUserCommandUseCase.createWorkspaceUser(any()))
                .thenThrow(new WorkspaceNotFoundException());

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.multipart("/api/workspace-users/admin")
                .file(profile)
                .file(createJsonFile(request))
                .header(HttpHeaders.AUTHORIZATION, "Bearer testAccessToken")
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.code", is(CommonErrorCode.APPLICATION_EXCEPTION.getCode())));
    }

    @DisplayName("워크스페이스 유저 관리자 생성 - 실패 / 유저가 없을 경우")
    @Test
    void create_workspace_user_fail_not_found_user() throws Exception {

        // given
        CreateWorkspaceUserRequest request = createCreateWorkspaceUserRequest();
        MockMultipartFile profile = FILE;
        when(workspaceUserCommandUseCase.createWorkspaceUser(any()))
                .thenThrow(new UserNotFoundException());

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.multipart("/api/workspace-users/admin")
                .file(profile)
                .file(createJsonFile(request))
                .header(HttpHeaders.AUTHORIZATION, "Bearer testAccessToken")
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.code", is(CommonErrorCode.APPLICATION_EXCEPTION.getCode())));
    }

    @DisplayName("워크스페이스 유저 관리자 생성 - 성공")
    @Test
    void create_workspace_success() throws Exception {

        // given
        CreateWorkspaceUserRequest request = createCreateWorkspaceUserRequest();
        MockMultipartFile profile = FILE;
        WorkspaceUserResponseDto responseDto = createWorkspaceUserAdminResponseDto();
        when(workspaceUserCommandUseCase.createWorkspaceUser(any()))
                .thenReturn(responseDto);

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.multipart("/api/workspace-users/admin")
                .file(profile)
                .file(createJsonFile(request))
                .header(HttpHeaders.AUTHORIZATION, "Bearer testAccessToken")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.workspaceUserId", is(responseDto.getWorkspaceUserId().intValue())))
                .andExpect(jsonPath("$.nickname", is(responseDto.getNickname())))
                .andExpect(jsonPath("$.workspaceAdmin", is(responseDto.isWorkspaceAdmin())))
                .andExpect(jsonPath("$.position", is(responseDto.getPosition())))
                .andExpect(jsonPath("$.profileImageUrl", is(responseDto.getProfileImageUrl())))
                .andExpect(jsonPath("$.email", is(responseDto.getContactMail())))
                .andExpect(jsonPath("$.phone", is(responseDto.getPhone())))
                .andDo(restDocumentationResultHandler.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access Token").attributes(field("constraints", "JWT Access Token With Bearer"))
                        ),
                        requestParts(
                                partWithName("files").optional().description("업로드할 프로필 이미지 파일"),
                                partWithName("request").description("요청하는 JSON Body")
                        ),
                        requestFields(
                                fieldWithPath("workspaceId").type(JsonFieldType.NUMBER).description("워크스페이스 ID"),
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임").attributes(field("constraints", "워크스페이스 내 중복될 경우 에러 발생, 5자 이하로 작성")),
                                fieldWithPath("position").type(JsonFieldType.STRING).description("직첵").attributes(field("constraints", "5자 이하로 작성")),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일").optional(),
                                fieldWithPath("phone").type(JsonFieldType.STRING).description("휴대전화번호").optional()
                        ),
                        responseFields(
                                fieldWithPath("workspaceUserId").type(JsonFieldType.NUMBER).description("생성된 워크스페이스 유저 ID"),
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("생성된 워크스페이스 유저 닉네임"),
                                fieldWithPath("workspaceAdmin").type(JsonFieldType.BOOLEAN).description("생성된 워크스페이스 유저의 관리자 여부"),
                                fieldWithPath("position").type(JsonFieldType.STRING).description("생성된 워크스페이스 유저 직책"),
                                fieldWithPath("profileImageUrl").type(JsonFieldType.STRING).description("생성된 워크스페이스 유저 프로필 이미지 URL, 없을 경우 \"\""),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("생성된 워크스페이스 유저의 연락용 메일, 없을 경우 \"\""),
                                fieldWithPath("phone").type(JsonFieldType.STRING).description("생성된 워크스페이스 유저의 연락용 휴대전화번호, 없을 경우 \"\"")
                        )
                ));
    }

    private WorkspaceUserResponseDto createWorkspaceUserAdminResponseDto() {
        return WorkspaceUserResponseDto.builder()
                .workspaceUserId(1L)
                .nickname("테스트닉네임")
                .workspaceAdmin(true)
                .position("개발자")
                .profileImageUrl("")
                .contactMail("")
                .phone("")
                .build();
    }

    @DisplayName("워크스페이스 일반 유저 생성 - 성공")
    @Test
    void create_workspace_common_success() throws Exception {

        // given
        CreateWorkspaceUserRequest request = createCreateWorkspaceUserRequest();
        MockMultipartFile profile = FILE;
        WorkspaceUserResponseDto responseDto = createWorkspaceUserResponseDto();
        when(workspaceUserCommandUseCase.createWorkspaceUser(any()))
                .thenReturn(responseDto);

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.multipart("/api/workspace-users")
                .file(profile)
                .file(createJsonFile(request))
                .header(HttpHeaders.AUTHORIZATION, "Bearer testAccessToken")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.workspaceUserId", is(responseDto.getWorkspaceUserId().intValue())))
                .andExpect(jsonPath("$.nickname", is(responseDto.getNickname())))
                .andExpect(jsonPath("$.workspaceAdmin", is(responseDto.isWorkspaceAdmin())))
                .andExpect(jsonPath("$.position", is(responseDto.getPosition())))
                .andExpect(jsonPath("$.profileImageUrl", is(responseDto.getProfileImageUrl())))
                .andExpect(jsonPath("$.email", is(responseDto.getContactMail())))
                .andExpect(jsonPath("$.phone", is(responseDto.getPhone())))
                .andDo(restDocumentationResultHandler.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access Token").attributes(field("constraints", "JWT Access Token With Bearer"))
                        ),
                        requestParts(
                                partWithName("files").optional().description("업로드할 프로필 이미지 파일"),
                                partWithName("request").description("요청하는 JSON Body")
                        ),
                        requestFields(
                                fieldWithPath("workspaceId").type(JsonFieldType.NUMBER).description("워크스페이스 ID"),
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임").attributes(field("constraints", "워크스페이스 내 중복될 경우 에러 발생, 5자 이하로 작성")),
                                fieldWithPath("position").type(JsonFieldType.STRING).description("직첵").attributes(field("constraints", "5자 이하로 작성")),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일").optional(),
                                fieldWithPath("phone").type(JsonFieldType.STRING).description("휴대전화번호").optional()
                        ),
                        responseFields(
                                fieldWithPath("workspaceUserId").type(JsonFieldType.NUMBER).description("생성된 워크스페이스 유저 ID"),
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("생성된 워크스페이스 유저 닉네임"),
                                fieldWithPath("workspaceAdmin").type(JsonFieldType.BOOLEAN).description("생성된 워크스페이스 유저의 관리자 여부"),
                                fieldWithPath("position").type(JsonFieldType.STRING).description("생성된 워크스페이스 유저 직책"),
                                fieldWithPath("profileImageUrl").type(JsonFieldType.STRING).description("생성된 워크스페이스 유저 프로필 이미지 URL, 없을 경우 \"\""),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("생성된 워크스페이스 유저의 연락용 메일, 없을 경우 \"\""),
                                fieldWithPath("phone").type(JsonFieldType.STRING).description("생성된 워크스페이스 유저의 연락용 휴대전화번호, 없을 경우 \"\"")
                        )
                ));
    }

    private WorkspaceUserResponseDto createWorkspaceUserResponseDto() {
        return WorkspaceUserResponseDto.builder()
                .workspaceUserId(1L)
                .nickname("테스트닉네임")
                .workspaceAdmin(false)
                .position("개발자")
                .profileImageUrl("")
                .contactMail("")
                .phone("")
                .build();
    }

    @DisplayName("닉네임 중복 체크 - 성공")
    @Test
    void check_duplicate_nickname_success() throws Exception {

        // given
        FindWorkspaceUserRequest request = FindWorkspaceUserRequestBuilder.build();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("workspaceId", request.getWorkspaceId().toString());
        params.add("nickname", request.getNickname());

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/workspace-users/nickname")
                .header(HttpHeaders.AUTHORIZATION, "Bearer testAccessToken")
                .params(params))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.duplicate", is(false)))
                .andDo(restDocumentationResultHandler.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access Token").attributes(field("constraints", "JWT Access Token With Bearer"))
                        ),
                        requestParameters(
                                parameterWithName("workspaceId").description("워크스페이스 ID"),
                                parameterWithName("nickname").description("검사할 유저 닉네임")
                        ),
                        responseFields(
                                fieldWithPath("duplicate").type(JsonFieldType.BOOLEAN).description("false 리턴 시 중복되지 않았음을 의미")
                        )
                ));
    }

    @DisplayName("닉네임 중복 체크 - 실패 / 닉네임이 중복되었을 경우")
    @Test
    void check_duplicate_nickname_fail_duplicate() throws Exception {

        // given
        doThrow(new NicknameDuplicateException())
                .when(workspaceUserQueryUseCase)
                .checkDuplicateNickname(any());
        FindWorkspaceUserRequest request = FindWorkspaceUserRequestBuilder.build();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("workspaceId", request.getWorkspaceId().toString());
        params.add("nickname", request.getNickname());

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/workspace-users/nickname")
                .header(HttpHeaders.AUTHORIZATION, "Bearer testAccessToken")
                .params(params))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.code", is(CommonErrorCode.APPLICATION_EXCEPTION.getCode())));
    }
}