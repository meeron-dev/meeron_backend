package com.cmc.meeron.workspaceuser.adapter.in;

import com.cmc.meeron.common.exception.ClientErrorCode;
import com.cmc.meeron.common.exception.file.FileErrorCode;
import com.cmc.meeron.common.exception.file.FileExtensionNotFoundException;
import com.cmc.meeron.common.exception.file.FileUploadException;
import com.cmc.meeron.common.exception.team.TeamErrorCode;
import com.cmc.meeron.common.exception.team.TeamNotFoundException;
import com.cmc.meeron.common.exception.user.UserErrorCode;
import com.cmc.meeron.common.exception.user.UserNotFoundException;
import com.cmc.meeron.common.exception.workspace.*;
import com.cmc.meeron.support.restdocs.RestDocsTestSupport;
import com.cmc.meeron.support.security.WithMockJwt;
import com.cmc.meeron.team.adapter.in.request.JoinTeamMembersRequest;
import com.cmc.meeron.team.adapter.in.request.JoinTeamMembersRequestBuilder;
import com.cmc.meeron.team.adapter.in.request.EjectTeamMemberRequestBuilder;
import com.cmc.meeron.team.adapter.in.request.EjectTeamMemberRequest;
import com.cmc.meeron.user.adapter.in.request.FindWorkspaceUserRequestBuilder;
import com.cmc.meeron.workspaceuser.adapter.in.request.*;
import com.cmc.meeron.workspaceuser.application.port.in.request.FindNoneTeamWorkspaceUsersParametersBuilder;
import com.cmc.meeron.workspaceuser.application.port.in.response.WorkspaceUserQueryResponseDtoBuilder;
import com.cmc.meeron.workspaceuser.application.port.in.response.WorkspaceUserResponseDto;
import com.cmc.meeron.workspaceuser.application.port.in.response.WorkspaceUserResponseDtoBuilder;
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
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.multipart;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockJwt
class WorkspaceUserRestControllerTest extends RestDocsTestSupport {


    @DisplayName("????????? ?????? ?????????????????? ?????? ????????? ???????????? - ??????")
    @Test
    void get_my_workspace_users_success() throws Exception {

        // given
        List<WorkspaceUserResponseDto> myWorkspaceUsers = WorkspaceUserQueryResponseDtoBuilder.buildList();
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
                .andExpect(jsonPath("$.myWorkspaceUsers[0].phone", is(myWorkspaceUsers.get(0).getPhone())))
                .andExpect(jsonPath("$.myWorkspaceUsers[1].workspaceUserId", is(myWorkspaceUsers.get(1).getWorkspaceUserId().intValue())))
                .andExpect(jsonPath("$.myWorkspaceUsers[1].workspaceId", is(myWorkspaceUsers.get(1).getWorkspaceId().intValue())))
                .andExpect(jsonPath("$.myWorkspaceUsers[1].workspaceAdmin", is(myWorkspaceUsers.get(1).isWorkspaceAdmin())))
                .andExpect(jsonPath("$.myWorkspaceUsers[1].nickname", is(myWorkspaceUsers.get(1).getNickname())))
                .andExpect(jsonPath("$.myWorkspaceUsers[1].profileImageUrl", is(myWorkspaceUsers.get(1).getProfileImageUrl())))
                .andExpect(jsonPath("$.myWorkspaceUsers[1].position", is(myWorkspaceUsers.get(1).getPosition())))
                .andExpect(jsonPath("$.myWorkspaceUsers[1].email", is(myWorkspaceUsers.get(1).getEmail())))
                .andExpect(jsonPath("$.myWorkspaceUsers[1].phone", is(myWorkspaceUsers.get(1).getPhone())))
                .andDo(restDocumentationResultHandler.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access Token").attributes(field("constraints", "JWT Access Token With Bearer"))
                        ),
                        pathParameters(
                                parameterWithName("userId").description("?????? ID")
                        ),
                        responseFields(
                                fieldWithPath("myWorkspaceUsers[].workspaceUserId").type(JsonFieldType.NUMBER).description("?????????????????? ?????? ID"),
                                fieldWithPath("myWorkspaceUsers[].profileImageUrl").type(JsonFieldType.STRING).optional().description("?????????????????? ?????? ????????? ????????? URL, ?????? ?????? \"\" ??????"),
                                fieldWithPath("myWorkspaceUsers[].nickname").type(JsonFieldType.STRING).description("?????? ?????????????????? ?????? ?????????"),
                                fieldWithPath("myWorkspaceUsers[].position").type(JsonFieldType.STRING).description("?????? ?????????????????? ?????? ??????, ?????? ?????? \"\" ??????"),
                                fieldWithPath("myWorkspaceUsers[].workspaceId").type(JsonFieldType.NUMBER).description("?????????????????? ????????? ?????? ?????????????????? ID"),
                                fieldWithPath("myWorkspaceUsers[].workspaceAdmin").type(JsonFieldType.BOOLEAN).description("?????????????????? ????????? ????????? ??????"),
                                fieldWithPath("myWorkspaceUsers[].email").type(JsonFieldType.STRING).description("?????????????????? ?????? ?????????, ?????? ?????? \"\" ??????"),
                                fieldWithPath("myWorkspaceUsers[].phone").type(JsonFieldType.STRING).description("?????????????????? ?????? ??????????????????, ?????? ?????? \"\" ??????")
                        )
                ));
    }

    @DisplayName("????????? ?????????????????? ?????? ??? ?????? ???????????? - ??????")
    @Test
    void get_my_workspace_user_success() throws Exception {

        // given
        WorkspaceUserResponseDto myWorkspaceUser = WorkspaceUserQueryResponseDtoBuilder.buildList().get(0);
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
                                parameterWithName("workspaceUserId").description("?????????????????? ?????? ID")
                        ),
                        responseFields(
                                fieldWithPath("workspaceUserId").type(JsonFieldType.NUMBER).description("????????? ?????????????????? ?????? ID"),
                                fieldWithPath("workspaceId").type(JsonFieldType.NUMBER).description("?????????????????? ????????? ?????? ?????????????????? ID"),
                                fieldWithPath("workspaceAdmin").type(JsonFieldType.BOOLEAN).description("?????????????????? ????????? ????????? ??????"),
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("?????????????????? ?????? ?????????"),
                                fieldWithPath("profileImageUrl").type(JsonFieldType.STRING).description("?????????????????? ?????? ????????? ????????? URL, ?????? ?????? \"\" ??????"),
                                fieldWithPath("position").type(JsonFieldType.STRING).description("?????????????????? ?????? ??????, ?????? ?????? \"\" ??????"),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("?????????????????? ?????? ?????????, ?????? ?????? \"\" ??????"),
                                fieldWithPath("phone").type(JsonFieldType.STRING).description("?????????????????? ?????? ??????????????????, ?????? ?????? \"\" ??????")
                        )
                ));
    }

    @DisplayName("????????? ????????? ?????????????????? ?????? ???????????? - ?????? / ?????? ????????? ?????? ??????")
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
                .andExpect(jsonPath("$.code", is(WorkspaceUserErrorCode.NOT_FOUND.getCode())));
    }

    @DisplayName("?????????????????? ?????? ????????? ?????? - ?????? / ??????????????? ????????? ?????? ??????")
    @Test
    void modify_workspace_user_fail_invalid() throws Exception {

        // given
        MockMultipartFile file = FILE;
        ModifyWorkspaceUserRequest request = ModifyWorkspaceUserRequestBuilder.buildInvalid();

        // when, then, docs
        mockMvc.perform(multipart("/api/workspace-users/{workspaceUserId}", 1L)
                .file(file)
                .file(createJsonFile(request))
                .with(req -> {
                    req.setMethod("PUT");
                    return req;
                })
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header(HttpHeaders.AUTHORIZATION, "Bearer testAccessToken"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.code", is(ClientErrorCode.BIND_EXCEPTION.getCode())))
                .andExpect(jsonPath("$.errors", hasSize(2)));
    }

    @DisplayName("?????????????????? ?????? ????????? ?????? - ?????? / ?????????????????? ????????? ???????????? ?????? ??????")
    @Test
    void modify_workspace_user_fail_not_found_workspace_user() throws Exception {

        // given
        MockMultipartFile file = FILE;
        ModifyWorkspaceUserRequest request = ModifyWorkspaceUserRequestBuilder.build();
        when(workspaceUserCommandUseCase.modifyWorkspaceUser(any()))
                .thenThrow(new WorkspaceUserNotFoundException());

        // when, then, docs
        mockMvc.perform(multipart("/api/workspace-users/{workspaceUserId}", 1L)
                .file(file)
                .file(createJsonFile(request))
                .with(req -> {
                    req.setMethod("PUT");
                    return req;
                })
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header(HttpHeaders.AUTHORIZATION, "Bearer testAccessToken"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.code", is(WorkspaceUserErrorCode.NOT_FOUND.getCode())));
    }

    @DisplayName("?????????????????? ?????? ????????? ?????? - ?????? / ?????? ???????????? ?????? ??????")
    @Test
    void modify_workspace_user_fail_not_found_file_extension() throws Exception {

        // given
        MockMultipartFile file = FILE;
        ModifyWorkspaceUserRequest request = ModifyWorkspaceUserRequestBuilder.build();
        when(workspaceUserCommandUseCase.modifyWorkspaceUser(any()))
                .thenThrow(new FileUploadException());

        // when, then, docs
        mockMvc.perform(multipart("/api/workspace-users/{workspaceUserId}", 1L)
                .file(file)
                .file(createJsonFile(request))
                .with(req -> {
                    req.setMethod("PUT");
                    return req;
                })
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header(HttpHeaders.AUTHORIZATION, "Bearer testAccessToken"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.code", is(FileErrorCode.FILE_UPLOAD.getCode())));
    }

    @DisplayName("?????????????????? ?????? ????????? ?????? - ?????? / ?????? ????????? ??? ????????? ????????? ??????")
    @Test
    void modify_workspace_user_fail_file_upload_exception() throws Exception {

        // given
        MockMultipartFile file = FILE;
        ModifyWorkspaceUserRequest request = ModifyWorkspaceUserRequestBuilder.build();
        when(workspaceUserCommandUseCase.modifyWorkspaceUser(any()))
                .thenThrow(new FileExtensionNotFoundException());

        // when, then, docs
        mockMvc.perform(multipart("/api/workspace-users/{workspaceUserId}", 1L)
                .file(file)
                .file(createJsonFile(request))
                .with(req -> {
                    req.setMethod("PUT");
                    return req;
                })
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header(HttpHeaders.AUTHORIZATION, "Bearer testAccessToken"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.code", is(FileErrorCode.FILE_EXTENSION_NOT_FOUND.getCode())));
    }

    @DisplayName("?????????????????? ?????? ????????? ?????? - ?????? / ???????????? ??????????????? ??????")
    @Test
    void modify_workspace_user_fail_duplicate_nickname() throws Exception {

        // given
        MockMultipartFile file = FILE;
        ModifyWorkspaceUserRequest request = ModifyWorkspaceUserRequestBuilder.build();
        when(workspaceUserCommandUseCase.modifyWorkspaceUser(any()))
                .thenThrow(new NicknameDuplicateException());

        // when, then, docs
        mockMvc.perform(multipart("/api/workspace-users/{workspaceUserId}", 1L)
                .file(file)
                .file(createJsonFile(request))
                .with(req -> {
                    req.setMethod("PUT");
                    return req;
                })
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header(HttpHeaders.AUTHORIZATION, "Bearer testAccessToken"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.code", is(WorkspaceUserErrorCode.DUPLICATE_NICKNAME.getCode())));
    }

    @DisplayName("?????????????????? ?????? ????????? ?????? - ??????")
    @Test
    void modify_workspace_user_success() throws Exception {

        // given
        WorkspaceUserResponseDto responseDto = WorkspaceUserResponseDtoBuilder.build();
        when(workspaceUserCommandUseCase.modifyWorkspaceUser(any()))
                .thenReturn(responseDto);
        MockMultipartFile file = FILE;
        ModifyWorkspaceUserRequest request = ModifyWorkspaceUserRequestBuilder.build();

        // when, then, docs
        mockMvc.perform(multipart("/api/workspace-users/{workspaceUserId}", 1L)
                .file(file)
                .file(createJsonFile(request))
                .with(req -> {
                    req.setMethod("PUT");
                    return req;
                })
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header(HttpHeaders.AUTHORIZATION, "Bearer testAccessToken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.workspaceUserId", is(responseDto.getWorkspaceUserId().intValue())))
                .andExpect(jsonPath("$.workspaceId", is(responseDto.getWorkspaceId().intValue())))
                .andExpect(jsonPath("$.workspaceAdmin", is(responseDto.isWorkspaceAdmin())))
                .andExpect(jsonPath("$.nickname", is(responseDto.getNickname())))
                .andExpect(jsonPath("$.position", is(responseDto.getPosition())))
                .andExpect(jsonPath("$.profileImageUrl", is(responseDto.getProfileImageUrl())))
                .andExpect(jsonPath("$.email", is(responseDto.getEmail())))
                .andExpect(jsonPath("$.phone", is(responseDto.getPhone())))
                .andDo(restDocumentationResultHandler.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access Token").attributes(field("constraints", "JWT Access Token With Bearer"))
                        ),
                        pathParameters(
                                parameterWithName("workspaceUserId").description("????????? ?????????????????? ?????? ID")
                        ),
                        requestParts(
                                partWithName("files").optional().description("???????????? ????????? ????????? ??????"),
                                partWithName("request").description("???????????? JSON Body")
                        ),
                        requestFields(
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("?????????").attributes(field("constraints", "?????????????????? ??? ????????? ?????? ?????? ??????, 5??? ????????? ??????")),
                                fieldWithPath("position").type(JsonFieldType.STRING).description("??????").attributes(field("constraints", "5??? ????????? ??????")),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("?????????").optional(),
                                fieldWithPath("phone").type(JsonFieldType.STRING).description("??????????????????").optional()
                        ),
                        responseFields(
                                fieldWithPath("workspaceUserId").type(JsonFieldType.NUMBER).description("????????? ?????????????????? ?????? ID"),
                                fieldWithPath("workspaceId").type(JsonFieldType.NUMBER).description("?????????????????? ID"),
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("????????? ?????????????????? ?????? ?????????"),
                                fieldWithPath("workspaceAdmin").type(JsonFieldType.BOOLEAN).description("????????? ?????????????????? ????????? ????????? ??????"),
                                fieldWithPath("position").type(JsonFieldType.STRING).description("????????? ?????????????????? ?????? ??????"),
                                fieldWithPath("profileImageUrl").type(JsonFieldType.STRING).description("????????? ?????????????????? ?????? ????????? ????????? URL"),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("????????? ?????????????????? ????????? ????????? ??????"),
                                fieldWithPath("phone").type(JsonFieldType.STRING).description("????????? ?????????????????? ????????? ????????? ??????????????????")
                        )
                ));
    }

    @DisplayName("?????????????????? ?????? ?????? - ??????")
    @Test
    void search_workspace_users_success() throws Exception {

        // given
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("workspaceId", "1");
        params.add("nickname", "?????????");
        List<WorkspaceUserResponseDto> workspaceUsers = WorkspaceUserQueryResponseDtoBuilder.buildList();
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
                .andExpect(jsonPath("$.workspaceUsers[0].phone", is(workspaceUsers.get(0).getPhone())))
                .andExpect(jsonPath("$.workspaceUsers[1].workspaceUserId", is(workspaceUsers.get(1).getWorkspaceUserId().intValue())))
                .andExpect(jsonPath("$.workspaceUsers[1].profileImageUrl", is(workspaceUsers.get(1).getProfileImageUrl())))
                .andExpect(jsonPath("$.workspaceUsers[1].nickname", is(workspaceUsers.get(1).getNickname())))
                .andExpect(jsonPath("$.workspaceUsers[1].position", is(workspaceUsers.get(1).getPosition())))
                .andExpect(jsonPath("$.workspaceUsers[1].workspaceAdmin", is(workspaceUsers.get(1).isWorkspaceAdmin())))
                .andExpect(jsonPath("$.workspaceUsers[1].workspaceId", is(workspaceUsers.get(1).getWorkspaceId().intValue())))
                .andExpect(jsonPath("$.workspaceUsers[1].phone", is(workspaceUsers.get(1).getPhone())))
                .andDo(restDocumentationResultHandler.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access Token").attributes(field("constraints", "JWT Access Token With Bearer"))
                        ),
                        requestParameters(
                                parameterWithName("workspaceId").description("?????????????????? ID"),
                                parameterWithName("nickname").description("?????? ?????????????????? ?????? ?????????")
                        ),
                        responseFields(
                                fieldWithPath("workspaceUsers[].workspaceUserId").type(JsonFieldType.NUMBER).description("?????????????????? ?????? ID"),
                                fieldWithPath("workspaceUsers[].profileImageUrl").type(JsonFieldType.STRING).optional().description("?????????????????? ?????? ????????? ????????? URL, ?????? ?????? \"\" ??????"),
                                fieldWithPath("workspaceUsers[].nickname").type(JsonFieldType.STRING).description("?????? ?????????????????? ?????? ?????????"),
                                fieldWithPath("workspaceUsers[].position").type(JsonFieldType.STRING).description("?????? ?????????????????? ?????? ??????, ?????? ?????? \"\" ??????"),
                                fieldWithPath("workspaceUsers[].workspaceId").type(JsonFieldType.NUMBER).description("?????????????????? ????????? ?????? ?????????????????? ID"),
                                fieldWithPath("workspaceUsers[].workspaceAdmin").type(JsonFieldType.BOOLEAN).description("?????????????????? ????????? ????????? ??????"),
                                fieldWithPath("workspaceUsers[].email").type(JsonFieldType.STRING).description("?????????????????? ?????? ?????????, ?????? ?????? \"\" ??????"),
                                fieldWithPath("workspaceUsers[].phone").type(JsonFieldType.STRING).description("?????????????????? ?????? ??????????????????, ?????? ?????? \"\" ??????")
                        )
                ));
    }

    @DisplayName("?????????????????? ?????? ?????? - ?????????????????? ID, ????????? ?????????????????? ?????? ???????????? ?????? ?????? ??????")
    @Test
    void search_workspace_users_fail_require_workspace_id_nickname() throws Exception {

        // given, when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/workspace-users")
                .header(HttpHeaders.AUTHORIZATION, "Bearer testAccessToken")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(2)))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.code", is(ClientErrorCode.BIND_EXCEPTION.getCode())));
    }

    @DisplayName("?????? ?????? ?????? ?????????????????? ?????? ?????? ?????? - ??????")
    @Test
    void get_team_members_success() throws Exception {

        // given
        List<WorkspaceUserResponseDto> workspaceUsers = WorkspaceUserQueryResponseDtoBuilder.buildList();
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
                .andExpect(jsonPath("$.workspaceUsers[0].phone", is(workspaceUsers.get(0).getPhone())))
                .andExpect(jsonPath("$.workspaceUsers[1].workspaceUserId", is(workspaceUsers.get(1).getWorkspaceUserId().intValue())))
                .andExpect(jsonPath("$.workspaceUsers[1].profileImageUrl", is(workspaceUsers.get(1).getProfileImageUrl())))
                .andExpect(jsonPath("$.workspaceUsers[1].nickname", is(workspaceUsers.get(1).getNickname())))
                .andExpect(jsonPath("$.workspaceUsers[1].position", is(workspaceUsers.get(1).getPosition())))
                .andExpect(jsonPath("$.workspaceUsers[1].workspaceAdmin", is(workspaceUsers.get(1).isWorkspaceAdmin())))
                .andExpect(jsonPath("$.workspaceUsers[1].workspaceId", is(workspaceUsers.get(1).getWorkspaceId().intValue())))
                .andExpect(jsonPath("$.workspaceUsers[1].phone", is(workspaceUsers.get(1).getPhone())))
                .andDo(restDocumentationResultHandler.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access Token").attributes(field("constraints", "JWT Access Token With Bearer"))
                        ),
                        pathParameters(
                                parameterWithName("teamId").description("???????????? ?????? ??? ID")
                        ),
                        responseFields(
                                fieldWithPath("workspaceUsers[].workspaceUserId").type(JsonFieldType.NUMBER).description("?????????????????? ?????? ID"),
                                fieldWithPath("workspaceUsers[].profileImageUrl").type(JsonFieldType.STRING).optional().description("?????????????????? ?????? ????????? ????????? URL, ?????? ?????? \"\" ??????"),
                                fieldWithPath("workspaceUsers[].nickname").type(JsonFieldType.STRING).description("?????? ?????????????????? ?????? ?????????"),
                                fieldWithPath("workspaceUsers[].position").type(JsonFieldType.STRING).description("?????? ?????????????????? ?????? ??????, ?????? ?????? \"\" ??????"),
                                fieldWithPath("workspaceUsers[].workspaceId").type(JsonFieldType.NUMBER).description("?????????????????? ????????? ?????? ?????????????????? ID"),
                                fieldWithPath("workspaceUsers[].workspaceAdmin").type(JsonFieldType.BOOLEAN).description("?????????????????? ????????? ????????? ??????"),
                                fieldWithPath("workspaceUsers[].email").type(JsonFieldType.STRING).description("?????????????????? ?????? ?????????, ?????? ?????? \"\" ??????"),
                                fieldWithPath("workspaceUsers[].phone").type(JsonFieldType.STRING).description("?????????????????? ?????? ??????????????????, ?????? ?????? \"\" ??????")
                        )
                ));
    }

    @DisplayName("?????????????????? ?????? ????????? ?????? - ?????? / ??????????????? ????????? ?????? ??????")
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
                .andExpect(jsonPath("$.code", is(ClientErrorCode.BIND_EXCEPTION.getCode())))
                .andExpect(jsonPath("$.errors", hasSize(3)));
    }

    @DisplayName("?????????????????? ?????? ?????????  ?????? - ?????? / ???????????? ????????? ??????")
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
                .andExpect(jsonPath("$.code", is(WorkspaceUserErrorCode.DUPLICATE_NICKNAME.getCode())));
    }

    private CreateWorkspaceUserRequest createCreateWorkspaceUserRequest() {
        return CreateWorkspaceUserRequest.builder()
                .workspaceId(1L)
                .nickname("?????????")
                .position("?????????")
                .email("test@test.com")
                .phone("010-1234-1234")
                .build();
    }

    @DisplayName("?????????????????? ?????? ????????? ?????? - ?????? / ????????????????????? ?????? ??????")
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
                .andExpect(jsonPath("$.code", is(WorkspaceErrorCode.NOT_FOUND.getCode())));
    }

    @DisplayName("?????????????????? ?????? ????????? ?????? - ?????? / ????????? ?????? ??????")
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
                .andExpect(jsonPath("$.code", is(UserErrorCode.NOT_FOUND_USER.getCode())));
    }

    @DisplayName("?????????????????? ?????? ????????? ?????? - ??????")
    @Test
    void create_workspace_user_admin_success() throws Exception {

        // given
        CreateWorkspaceUserRequest request = createCreateWorkspaceUserRequest();
        MockMultipartFile profile = FILE;
        WorkspaceUserResponseDto responseDto = WorkspaceUserResponseDtoBuilder.build();
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
                .andExpect(jsonPath("$.workspaceId", is(responseDto.getWorkspaceId().intValue())))
                .andExpect(jsonPath("$.nickname", is(responseDto.getNickname())))
                .andExpect(jsonPath("$.workspaceAdmin", is(responseDto.isWorkspaceAdmin())))
                .andExpect(jsonPath("$.position", is(responseDto.getPosition())))
                .andExpect(jsonPath("$.profileImageUrl", is(responseDto.getProfileImageUrl())))
                .andExpect(jsonPath("$.email", is(responseDto.getEmail())))
                .andExpect(jsonPath("$.phone", is(responseDto.getPhone())))
                .andDo(restDocumentationResultHandler.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access Token").attributes(field("constraints", "JWT Access Token With Bearer"))
                        ),
                        requestParts(
                                partWithName("files").optional().description("???????????? ????????? ????????? ??????"),
                                partWithName("request").description("???????????? JSON Body")
                        ),
                        requestFields(
                                fieldWithPath("workspaceId").type(JsonFieldType.NUMBER).description("?????????????????? ID"),
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("?????????").attributes(field("constraints", "?????????????????? ??? ????????? ?????? ?????? ??????, 5??? ????????? ??????")),
                                fieldWithPath("position").type(JsonFieldType.STRING).description("??????").attributes(field("constraints", "5??? ????????? ??????")),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("?????????").optional(),
                                fieldWithPath("phone").type(JsonFieldType.STRING).description("??????????????????").optional()
                        ),
                        responseFields(
                                fieldWithPath("workspaceUserId").type(JsonFieldType.NUMBER).description("????????? ?????????????????? ?????? ID"),
                                fieldWithPath("workspaceId").type(JsonFieldType.NUMBER).description("?????????????????? ID"),
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("????????? ?????????????????? ?????? ?????????"),
                                fieldWithPath("workspaceAdmin").type(JsonFieldType.BOOLEAN).description("????????? ?????????????????? ????????? ????????? ??????"),
                                fieldWithPath("position").type(JsonFieldType.STRING).description("????????? ?????????????????? ?????? ??????"),
                                fieldWithPath("profileImageUrl").type(JsonFieldType.STRING).description("????????? ?????????????????? ?????? ????????? ????????? URL"),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("????????? ?????????????????? ????????? ????????? ??????"),
                                fieldWithPath("phone").type(JsonFieldType.STRING).description("????????? ?????????????????? ????????? ????????? ??????????????????")
                        )
                ));
    }

    @DisplayName("?????????????????? ?????? ?????? ?????? - ??????")
    @Test
    void create_workspace_user_success() throws Exception {

        // given
        CreateWorkspaceUserRequest request = createCreateWorkspaceUserRequest();
        MockMultipartFile profile = FILE;
        WorkspaceUserResponseDto responseDto = WorkspaceUserResponseDtoBuilder.build();
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
                .andExpect(jsonPath("$.workspaceId", is(responseDto.getWorkspaceId().intValue())))
                .andExpect(jsonPath("$.nickname", is(responseDto.getNickname())))
                .andExpect(jsonPath("$.workspaceAdmin", is(responseDto.isWorkspaceAdmin())))
                .andExpect(jsonPath("$.position", is(responseDto.getPosition())))
                .andExpect(jsonPath("$.profileImageUrl", is(responseDto.getProfileImageUrl())))
                .andExpect(jsonPath("$.email", is(responseDto.getEmail())))
                .andExpect(jsonPath("$.phone", is(responseDto.getPhone())))
                .andDo(restDocumentationResultHandler.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access Token").attributes(field("constraints", "JWT Access Token With Bearer"))
                        ),
                        requestParts(
                                partWithName("files").optional().description("???????????? ????????? ????????? ??????"),
                                partWithName("request").description("???????????? JSON Body")
                        ),
                        requestFields(
                                fieldWithPath("workspaceId").type(JsonFieldType.NUMBER).description("?????????????????? ID"),
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("?????????").attributes(field("constraints", "?????????????????? ??? ????????? ?????? ?????? ??????, 5??? ????????? ??????")),
                                fieldWithPath("position").type(JsonFieldType.STRING).description("??????").attributes(field("constraints", "5??? ????????? ??????")),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("?????????").optional(),
                                fieldWithPath("phone").type(JsonFieldType.STRING).description("??????????????????").optional()
                        ),
                        responseFields(
                                fieldWithPath("workspaceUserId").type(JsonFieldType.NUMBER).description("????????? ?????????????????? ?????? ID"),
                                fieldWithPath("workspaceId").type(JsonFieldType.NUMBER).description("?????????????????? ID"),
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("????????? ?????????????????? ?????? ?????????"),
                                fieldWithPath("workspaceAdmin").type(JsonFieldType.BOOLEAN).description("????????? ?????????????????? ????????? ????????? ??????"),
                                fieldWithPath("position").type(JsonFieldType.STRING).description("????????? ?????????????????? ?????? ??????"),
                                fieldWithPath("profileImageUrl").type(JsonFieldType.STRING).description("????????? ?????????????????? ?????? ????????? ????????? URL"),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("????????? ?????????????????? ????????? ????????? ??????"),
                                fieldWithPath("phone").type(JsonFieldType.STRING).description("????????? ?????????????????? ????????? ????????? ??????????????????")
                        )
                ));
    }

    @DisplayName("????????? ?????? ?????? - ??????")
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
                                parameterWithName("workspaceId").description("?????????????????? ID"),
                                parameterWithName("nickname").description("????????? ?????? ?????????")
                        ),
                        responseFields(
                                fieldWithPath("duplicate").type(JsonFieldType.BOOLEAN).description("false ?????? ??? ???????????? ???????????? ??????")
                        )
                ));
    }

    @DisplayName("????????? ?????? ?????? - ?????? / ???????????? ??????????????? ??????")
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
                .andExpect(jsonPath("$.code", is(WorkspaceUserErrorCode.DUPLICATE_NICKNAME.getCode())));
    }

    @DisplayName("?????? ?????? - ?????? / ??????????????? ????????? ?????? ??????")
    @Test
    void join_team_users_fail_not_valid() throws Exception {

        // given
        JoinTeamMembersRequest request = JoinTeamMembersRequestBuilder.buildNotValid();

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/teams/{teamId}/workspace-users", "1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer testAccessToken")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.code", is(ClientErrorCode.BIND_EXCEPTION.getCode())))
                .andExpect(jsonPath("$.errors", hasSize(2)));
    }

    @DisplayName("?????? ?????? - ?????? / ?????? ???????????? ?????? ??????")
    @Test
    void join_team_users_fail_not_found_team() throws Exception {

        // given
        JoinTeamMembersRequest request = JoinTeamMembersRequestBuilder.build();
        doThrow(new TeamNotFoundException())
                .when(workspaceUserCommandUseCase)
                .joinTeamUsers(any());

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/teams/{teamId}/workspace-users", "1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer testAccessToken")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.code", is(TeamErrorCode.NOT_FOUND_TEAM.getCode())));
    }

    @DisplayName("?????? ?????? - ?????? / ?????? ???????????? ?????? ?????? ?????? ?????? ??????")
    @Test
    void join_team_users_fail_invalid_find_workspace_users_count() throws Exception {

        // given
        JoinTeamMembersRequest request = JoinTeamMembersRequestBuilder.build();
        doThrow(new NotAllFoundWorkspaceUsersException())
                .when(workspaceUserCommandUseCase)
                .joinTeamUsers(any());

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/teams/{teamId}/workspace-users", "1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer testAccessToken")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.code", is(WorkspaceUserErrorCode.NOT_ALL_FOUND.getCode())));
    }
    
    @DisplayName("?????? ?????? - ??????")
    @Test
    void join_team_users_success() throws Exception {
        
        // given
        JoinTeamMembersRequest request = JoinTeamMembersRequestBuilder.build();
        
        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/teams/{teamId}/workspace-users", "1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer testAccessToken")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(restDocumentationResultHandler.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access Token").attributes(field("constraints", "JWT Access Token With Bearer"))
                        ),
                        pathParameters(
                                parameterWithName("teamId").description("????????? ????????? ??? ID")
                        ),
                        requestFields(
                                fieldWithPath("adminWorkspaceUserId").type(JsonFieldType.NUMBER).description("????????? ?????????????????? ID (?????? ?????????)"),
                                fieldWithPath("joinTeamWorkspaceUserIds").type(JsonFieldType.ARRAY).description("?????? ?????? ?????? ?????????????????? ?????? ID").attributes(field("constraints", "?????? ????????? ID??? ???????????? ??? ???."))
                        )
                ));
    }

    @DisplayName("????????? ?????? - ?????? / ??????????????? ????????? ?????? ??????")
    @Test
    void kick_out_team_user_fail_invalid() throws Exception {

        // given
        EjectTeamMemberRequest request = EjectTeamMemberRequestBuilder.buildInvalid();

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/workspace-users/{workspaceUserId}/team", "1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer testAccessToken")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.code", is(ClientErrorCode.BIND_EXCEPTION.getCode())))
                .andExpect(jsonPath("$.errors", hasSize(1)));
    }

    @DisplayName("????????? ?????? - ?????? / ????????? ?????? ?????? ??????")
    @Test
    void kick_out_team_user_fail_not_found_workspace_user() throws Exception {

        // given
        EjectTeamMemberRequest request = EjectTeamMemberRequestBuilder.build();
        doThrow(new WorkspaceUserNotFoundException())
                .when(workspaceUserCommandUseCase)
                .kickOutTeamUser(any());

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/workspace-users/{workspaceUserId}/team", "1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer testAccessToken")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.code", is(WorkspaceUserErrorCode.NOT_FOUND.getCode())));
    }

    @DisplayName("????????? ?????? / ??????")
    @Test
    void kick_out_team_user_success() throws Exception {

        // given
        EjectTeamMemberRequest request = EjectTeamMemberRequestBuilder.build();

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/workspace-users/{workspaceUserId}/team", "1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer testAccessToken")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(restDocumentationResultHandler.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access Token").attributes(field("constraints", "JWT Access Token With Bearer"))
                        ),
                        pathParameters(
                                parameterWithName("workspaceUserId").description("????????? ?????????????????? ?????? ID")
                        ),
                        requestFields(
                                fieldWithPath("adminWorkspaceUserId").type(JsonFieldType.NUMBER).description("????????? ?????????????????? ID (?????? ?????????)")
                        )
                ));
    }

    @DisplayName("?????? ???????????? ?????? ?????? ?????? - ?????? / ?????? ??????????????? ?????? ?????? ??????")
    @Test
    void get_none_team_users_fail_invalid_parameters() throws Exception {

        // given, when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/teams/none/workspace-users")
                .header(HttpHeaders.AUTHORIZATION, "Bearer testAccessToken")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.code", is(ClientErrorCode.BIND_EXCEPTION.getCode())))
                .andExpect(jsonPath("$.errors", hasSize(1)));
    }

    @DisplayName("?????? ???????????? ?????? ?????? ?????? - ??????")
    @Test
    void get_none_team_users_success() throws Exception {

        // given
        FindNoneTeamWorkspaceUsersParameters parameters = FindNoneTeamWorkspaceUsersParametersBuilder.build();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("workspaceId", parameters.getWorkspaceId().toString());

        List<WorkspaceUserResponseDto> responseDtos = WorkspaceUserQueryResponseDtoBuilder.buildList();
        when(workspaceUserQueryUseCase.getNoneTeamWorkspaceUsers(any()))
                .thenReturn(responseDtos);

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/teams/none/workspace-users")
                .header(HttpHeaders.AUTHORIZATION, "Bearer testAccessToken")
                .params(params)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.workspaceUsers", hasSize(2)))
                .andExpect(jsonPath("$.workspaceUsers[0].workspaceUserId", is(responseDtos.get(0).getWorkspaceUserId().intValue())))
                .andExpect(jsonPath("$.workspaceUsers[0].profileImageUrl", is(responseDtos.get(0).getProfileImageUrl())))
                .andExpect(jsonPath("$.workspaceUsers[0].nickname", is(responseDtos.get(0).getNickname())))
                .andExpect(jsonPath("$.workspaceUsers[0].position", is(responseDtos.get(0).getPosition())))
                .andExpect(jsonPath("$.workspaceUsers[0].workspaceAdmin", is(responseDtos.get(0).isWorkspaceAdmin())))
                .andExpect(jsonPath("$.workspaceUsers[0].workspaceId", is(responseDtos.get(0).getWorkspaceId().intValue())))
                .andExpect(jsonPath("$.workspaceUsers[1].workspaceUserId", is(responseDtos.get(1).getWorkspaceUserId().intValue())))
                .andExpect(jsonPath("$.workspaceUsers[1].profileImageUrl", is(responseDtos.get(1).getProfileImageUrl())))
                .andExpect(jsonPath("$.workspaceUsers[1].nickname", is(responseDtos.get(1).getNickname())))
                .andExpect(jsonPath("$.workspaceUsers[1].position", is(responseDtos.get(1).getPosition())))
                .andExpect(jsonPath("$.workspaceUsers[1].workspaceAdmin", is(responseDtos.get(1).isWorkspaceAdmin())))
                .andExpect(jsonPath("$.workspaceUsers[1].workspaceId", is(responseDtos.get(1).getWorkspaceId().intValue())))
                .andDo(restDocumentationResultHandler.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access Token").attributes(field("constraints", "JWT Access Token With Bearer"))
                        ),
                        requestParameters(
                                parameterWithName("workspaceId").description("?????????????????? ID")
                        ),
                        responseFields(
                                fieldWithPath("workspaceUsers[].workspaceUserId").type(JsonFieldType.NUMBER).description("?????????????????? ?????? ID"),
                                fieldWithPath("workspaceUsers[].profileImageUrl").type(JsonFieldType.STRING).optional().description("?????????????????? ?????? ????????? ????????? URL, ?????? ?????? \"\" ??????"),
                                fieldWithPath("workspaceUsers[].nickname").type(JsonFieldType.STRING).description("?????? ?????????????????? ?????? ?????????"),
                                fieldWithPath("workspaceUsers[].position").type(JsonFieldType.STRING).description("?????? ?????????????????? ?????? ??????, ?????? ?????? \"\" ??????"),
                                fieldWithPath("workspaceUsers[].workspaceId").type(JsonFieldType.NUMBER).description("?????????????????? ????????? ?????? ?????????????????? ID"),
                                fieldWithPath("workspaceUsers[].workspaceAdmin").type(JsonFieldType.BOOLEAN).description("?????????????????? ????????? ????????? ??????"),
                                fieldWithPath("workspaceUsers[].email").type(JsonFieldType.STRING).description("?????????????????? ?????? ?????????, ?????? ?????? \"\" ??????"),
                                fieldWithPath("workspaceUsers[].phone").type(JsonFieldType.STRING).description("?????????????????? ?????? ??????????????????, ?????? ?????? \"\" ??????")
                        )
                ));
    }
}
