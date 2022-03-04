package com.cmc.meeron.team.adapter.in;

import com.cmc.meeron.support.restdocs.RestDocsTestSupport;
import com.cmc.meeron.support.security.WithMockJwt;
import com.cmc.meeron.team.application.port.in.response.WorkspaceTeamsResponseDto;
import com.google.common.net.HttpHeaders;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static com.cmc.meeron.config.RestDocsConfig.field;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockJwt
class TeamRestControllerTest extends RestDocsTestSupport {

    @DisplayName("워크스페이스 내 팀 조회 - 성공")
    @Test
    void get_workspace_teams_success() throws Exception {

        // given
        List<WorkspaceTeamsResponseDto> workspaceTeamsResponseDtos = createWorkspaceTeamsResponseDtos();
        when(teamQueryUseCase.getWorkspaceTeams(any()))
                .thenReturn(workspaceTeamsResponseDtos);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("workspaceId", "1");

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/teams")
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .params(params))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.teams", hasSize(2)))
                .andExpect(jsonPath("$.teams[0].teamId", is(workspaceTeamsResponseDtos.get(0).getTeamId().intValue())))
                .andExpect(jsonPath("$.teams[0].teamName", is(workspaceTeamsResponseDtos.get(0).getTeamName())))
                .andExpect(jsonPath("$.teams[1].teamId", is(workspaceTeamsResponseDtos.get(1).getTeamId().intValue())))
                .andExpect(jsonPath("$.teams[1].teamName", is(workspaceTeamsResponseDtos.get(1).getTeamName())))
                .andDo(restDocumentationResultHandler.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access Token").attributes(field("constraints", "JWT Access Token With Bearer"))
                        ),
                        requestParameters(
                                parameterWithName("workspaceId").description("워크스페이스 ID")
                        ),
                        responseFields(
                                fieldWithPath("teams[].teamId").type(JsonFieldType.NUMBER).description("워크스페이스 내의 팀 ID"),
                                fieldWithPath("teams[].teamName").type(JsonFieldType.STRING).description("워크스페이스 내의 팀 명")
                        )
                ));
    }

    private List<WorkspaceTeamsResponseDto> createWorkspaceTeamsResponseDtos() {
        return List.of(
                WorkspaceTeamsResponseDto.builder().teamId(1L).teamName("첫번째 팀").build(),
                WorkspaceTeamsResponseDto.builder().teamId(2L).teamName("두번째 팀").build()
        );
    }
}
