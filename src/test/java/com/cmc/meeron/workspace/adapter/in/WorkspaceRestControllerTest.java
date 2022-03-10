package com.cmc.meeron.workspace.adapter.in;

import com.cmc.meeron.common.exception.CommonErrorCode;
import com.cmc.meeron.common.exception.workspace.WorkspaceNotFoundException;
import com.cmc.meeron.support.restdocs.RestDocsTestSupport;
import com.cmc.meeron.support.security.WithMockJwt;
import com.cmc.meeron.workspace.application.port.in.response.WorkspaceResponseDto;
import com.google.common.net.HttpHeaders;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockJwt
class WorkspaceRestControllerTest extends RestDocsTestSupport {

    @DisplayName("유저가 속한 모든 워크스페이스 조회 - 성공")
    @Test
    void get_user_workspaces_success() throws Exception {

        // given
        List<WorkspaceResponseDto> myWorkspaces = createMyWorkspaces();
        when(workspaceQueryUseCase.getMyWorkspaces(any()))
                .thenReturn(myWorkspaces);

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/users/{userId}/workspaces", "1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.myWorkspaces", hasSize(myWorkspaces.size())))
                .andExpect(jsonPath("$.myWorkspaces[0].workspaceId", is(myWorkspaces.get(0).getWorkspaceId().intValue())))
                .andExpect(jsonPath("$.myWorkspaces[0].workspaceName", is(myWorkspaces.get(0).getWorkspaceName())))
                .andExpect(jsonPath("$.myWorkspaces[0].workspaceLogoUrl", is(myWorkspaces.get(0).getWorkspaceLogoUrl())))
                .andExpect(jsonPath("$.myWorkspaces[1].workspaceId", is(myWorkspaces.get(1).getWorkspaceId().intValue())))
                .andExpect(jsonPath("$.myWorkspaces[1].workspaceName", is(myWorkspaces.get(1).getWorkspaceName())))
                .andExpect(jsonPath("$.myWorkspaces[1].workspaceLogoUrl", is(myWorkspaces.get(1).getWorkspaceLogoUrl())))
                .andDo(restDocumentationResultHandler.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access Token")
                        ),
                        pathParameters(
                                parameterWithName("userId").description("워크스페이스를 찾으려는 유저 ID")
                        ),
                        responseFields(
                                fieldWithPath("myWorkspaces[].workspaceId").type(JsonFieldType.NUMBER).description("워크스페이스 ID"),
                                fieldWithPath("myWorkspaces[].workspaceName").type(JsonFieldType.STRING).description("워크스페이스 명"),
                                fieldWithPath("myWorkspaces[].workspaceLogoUrl").type(JsonFieldType.STRING).description("워크스페이스 로고 이미지 URL")
                        )
                ));
    }

    private List<WorkspaceResponseDto> createMyWorkspaces() {
        return List.of(
                createWorkspaceResponseDto(),
                WorkspaceResponseDto.builder()
                        .workspaceId(2L)
                        .workspaceName("테스트 워크스페이스명2")
                        .workspaceLogoUrl("https://www.image.com/123124")
                        .build()
        );
    }

    private WorkspaceResponseDto createWorkspaceResponseDto() {
        return WorkspaceResponseDto.builder()
                .workspaceId(1L)
                .workspaceName("테스트 워크스페이스명1")
                .workspaceLogoUrl("https://www.image.com/123123")
                .build();
    }

    @DisplayName("워크스페이스 조회 - 성공")
    @Test
    void get_workspace_success() throws Exception {

        // given
        WorkspaceResponseDto workspaceResponseDto = createWorkspaceResponseDto();
        when(workspaceQueryUseCase.getWorkspace(any()))
                .thenReturn(workspaceResponseDto);

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/workspaces/{workspaceId}", "1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.workspaceId", is(workspaceResponseDto.getWorkspaceId().intValue())))
                .andExpect(jsonPath("$.workspaceName", is(workspaceResponseDto.getWorkspaceName())))
                .andExpect(jsonPath("$.workspaceLogoUrl", is(workspaceResponseDto.getWorkspaceLogoUrl())))
                .andDo(restDocumentationResultHandler.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access Token")
                        ),
                        pathParameters(
                                parameterWithName("workspaceId").description("찾으려는 워크스페이스 ID")
                        ),
                        responseFields(
                                fieldWithPath("workspaceId").type(JsonFieldType.NUMBER).description("워크스페이스 ID"),
                                fieldWithPath("workspaceName").type(JsonFieldType.STRING).description("워크스페이스 명"),
                                fieldWithPath("workspaceLogoUrl").type(JsonFieldType.STRING).description("워크스페이스 로고 이미지 URL")
                        )
                ));
    }

    @DisplayName("워크스페이스 조회 - 실패 / 찾으려는 워크스페이스가 존재하지 않을 경우")
    @Test
    void get_workspace_fail_not_found_workspace() throws Exception {

        // given
        when(workspaceQueryUseCase.getWorkspace(any()))
                .thenThrow(new WorkspaceNotFoundException());

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/workspaces/{workspaceId}", "1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.message", notNullValue()))
                .andExpect(jsonPath("$.code", is(CommonErrorCode.APPLICATION_EXCEPTION.getCode())));
    }
}
