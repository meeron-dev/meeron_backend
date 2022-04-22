package com.cmc.meeron.team.adapter.in;

import com.cmc.meeron.common.exception.ClientErrorCode;
import com.cmc.meeron.common.exception.team.TeamCountsConditionException;
import com.cmc.meeron.common.exception.team.TeamErrorCode;
import com.cmc.meeron.common.exception.team.TeamNotFoundException;
import com.cmc.meeron.support.TestImproved;
import com.cmc.meeron.support.restdocs.RestDocsTestSupport;
import com.cmc.meeron.support.security.WithMockJwt;
import com.cmc.meeron.team.adapter.in.request.*;
import com.cmc.meeron.team.application.port.in.response.TeamResponseDto;
import com.cmc.meeron.team.application.port.in.response.TeamResponseDtoBuilder;
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
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockJwt
class TeamRestControllerTest extends RestDocsTestSupport {

    @Deprecated
    @DisplayName("워크스페이스 내 팀 조회 - 성공")
    @Test
    void get_workspace_teams_success() throws Exception {

        // given
        List<TeamResponseDto> responseDtos = TeamResponseDtoBuilder.buildList();
        when(teamQueryUseCase.getWorkspaceTeams(any()))
                .thenReturn(responseDtos);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("workspaceId", "1");

        // when, then, docs
        TeamResponseDto one = responseDtos.get(0);
        TeamResponseDto two = responseDtos.get(1);
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/teams")
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .params(params))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.teams", hasSize(2)))
                .andExpect(jsonPath("$.teams[0].teamId", is(one.getTeamId().intValue())))
                .andExpect(jsonPath("$.teams[0].teamName", is(one.getTeamName())))
                .andExpect(jsonPath("$.teams[1].teamId", is(two.getTeamId().intValue())))
                .andExpect(jsonPath("$.teams[1].teamName", is(two.getTeamName())))
                .andExpect(handler().handlerType(TeamRestController.class))
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

    @TestImproved(originMethod = "get_workspace_teams_success")
    @DisplayName("워크스페이스 내 팀 조회 - 성공")
    @Test
    void get_workspace_teams_success_v2() throws Exception {

        // given
        List<TeamResponseDto> responseDtos = TeamResponseDtoBuilder.buildList();
        when(teamQueryUseCase.getWorkspaceTeams(any()))
                .thenReturn(responseDtos);

        // when, then, docs
        TeamResponseDto one = responseDtos.get(0);
        TeamResponseDto two = responseDtos.get(1);
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/workspaces/{workspaceId}/teams", "1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.teams", hasSize(2)))
                .andExpect(jsonPath("$.teams[0].teamId", is(one.getTeamId().intValue())))
                .andExpect(jsonPath("$.teams[0].teamName", is(one.getTeamName())))
                .andExpect(jsonPath("$.teams[1].teamId", is(two.getTeamId().intValue())))
                .andExpect(jsonPath("$.teams[1].teamName", is(two.getTeamName())))
                .andExpect(handler().handlerType(TeamRestController.class))
                .andDo(restDocumentationResultHandler.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access Token").attributes(field("constraints", "JWT Access Token With Bearer"))
                        ),
                        pathParameters(
                                parameterWithName("workspaceId").description("워크스페이스 ID")
                        ),
                        responseFields(
                                fieldWithPath("teams[].teamId").type(JsonFieldType.NUMBER).description("워크스페이스 내의 팀 ID"),
                                fieldWithPath("teams[].teamName").type(JsonFieldType.STRING).description("워크스페이스 내의 팀 명")
                        )
                ));
    }

    @DisplayName("팀 생성 - 성공")
    @Test
    void create_team_success() throws Exception {

        // given
        CreateTeamRequest request = createCreateTeamRequest();
        when(teamCommandUseCase.createTeam(any()))
                .thenReturn(1L);

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/teams")
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(TeamRestController.class))
                .andDo(restDocumentationResultHandler.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access Token").attributes(field("constraints", "JWT Access Token With Bearer"))
                        ),
                        requestFields(
                                fieldWithPath("workspaceId").type(JsonFieldType.NUMBER).description("팀을 생성할 워크스페이스 ID"),
                                fieldWithPath("teamName").type(JsonFieldType.STRING).description("생성할 팀명").attributes(field("constraints", "1자 이상 10자 이하의 팀명"))
                        ),
                        responseFields(
                                fieldWithPath("createdTeamId").type(JsonFieldType.NUMBER).description("생성된 팀 ID")
                        )
                ));
    }

    @DisplayName("팀 생성 - 실패 / 팀이 다섯개를 넘어가는 경우")
    @Test
    void create_team_fail_over_five_teams() throws Exception {

        // given
        CreateTeamRequest request = createCreateTeamRequest();
        when(teamCommandUseCase.createTeam(any()))
                .thenThrow(new TeamCountsConditionException());

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/teams")
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(handler().handlerType(TeamRestController.class))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.code", is(TeamErrorCode.WORKSPACE_IN_TEAM_COUNT_OVER.getCode())));
    }

    public CreateTeamRequest createCreateTeamRequest() {
        return CreateTeamRequest.builder()
                .teamName("테스트")
                .workspaceId(1L)
                .build();
    }

    @DisplayName("팀 생성 - 실패 / 제약조건을 지키지 않을 경우")
    @Test
    void create_team_fail_not_valid() throws Exception {

        // given
        CreateTeamRequest request = CreateTeamRequest.builder()
                .teamName("                                  ").build();

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/teams")
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(handler().handlerType(TeamRestController.class))
                .andExpect(jsonPath("$.errors", hasSize(3)))
                .andExpect(jsonPath("$.code", is(ClientErrorCode.BIND_EXCEPTION.getCode())))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())));
    }

    @DisplayName("팀 삭제 - 실패 / 제약조건을 지키지 않을 경우")
    @Test
    void delete_team_fail_invalid() throws Exception {

        // given
        DeleteTeamRequest request = DeleteTeamRequestBuilder.buildNotValid();

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/teams/{teamId}", 1L)
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(handler().handlerType(TeamRestController.class))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.code", is(ClientErrorCode.BIND_EXCEPTION.getCode())))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())));
    }

    @DisplayName("팀 삭제 - 성공")
    @Test
    void delete_team_success() throws Exception {

        // given
        DeleteTeamRequest request = DeleteTeamRequestBuilder.build();

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/teams/{teamId}", 1L)
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(handler().handlerType(TeamRestController.class))
                .andDo(restDocumentationResultHandler.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access Token").attributes(field("constraints", "JWT Access Token With Bearer"))
                        ),
                        pathParameters(
                                parameterWithName("teamId").description("삭제할 팀 ID")
                        ),
                        requestFields(
                                fieldWithPath("adminWorkspaceUserId").type(JsonFieldType.NUMBER).description("요청자 워크스페이스 ID (권한 체크용)")
                        )
                ));

    }

    @DisplayName("팀명 변경 - 실패 / 제약조건을 지키지 않을 경우")
    @Test
    void modify_team_name_fail_not_valid() throws Exception {

        // given
        ModifyTeamNameRequest request = ModifyTeamNameRequestBuilder.notValid();

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/teams/{teamId}/name", 1L)
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(handler().handlerType(TeamRestController.class))
                .andExpect(jsonPath("$.errors", hasSize(3)))
                .andExpect(jsonPath("$.code", is(ClientErrorCode.BIND_EXCEPTION.getCode())))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())));
    }

    @DisplayName("팀명 변경 - 성공")
    @Test
    void modify_team_name_success() throws Exception {

        // given
        ModifyTeamNameRequest request = ModifyTeamNameRequestBuilder.build();

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/teams/{teamId}/name", 1L)
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(handler().handlerType(TeamRestController.class))
                .andDo(restDocumentationResultHandler.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access Token").attributes(field("constraints", "JWT Access Token With Bearer"))
                        ),
                        pathParameters(
                                parameterWithName("teamId").description("팀명을 변경할 팀 ID")
                        ),
                        requestFields(
                                fieldWithPath("adminWorkspaceUserId").type(JsonFieldType.NUMBER).description("요청자 워크스페이스 ID (권한 체크용)"),
                                fieldWithPath("teamName").type(JsonFieldType.STRING).description("변경할 팀명")
                        )
                ));
    }

    @DisplayName("회의를 주관하는 팀 조회 - 실패 / 팀이 없을 경우")
    @Test
    void get_host_team_fail_not_found() throws Exception {

        // given
        when(teamQueryUseCase.getMeetingHostTeam(any()))
                .thenThrow(new TeamNotFoundException());

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/meetings/{meetingId}/host-team",
                "1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(handler().handlerType(TeamRestController.class))
                .andExpect(jsonPath("$.code", is(TeamErrorCode.NOT_FOUND_TEAM.getCode())))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())));
    }

    @DisplayName("회의를 주관하는 팀 조회 -성공")
    @Test
    void get_host_team_success() throws Exception {

        // given
        TeamResponseDto responseDto = TeamResponseDtoBuilder.build();
        when(teamQueryUseCase.getMeetingHostTeam(any()))
                .thenReturn(responseDto);

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/meetings/{meetingId}/host-team",
                "1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(TeamRestController.class))
                .andExpect(jsonPath("$.teamId", is(responseDto.getTeamId().intValue())))
                .andExpect(jsonPath("$.teamName", is(responseDto.getTeamName())))
                .andDo(restDocumentationResultHandler.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access Token").attributes(field("constraints", "JWT Access Token With Bearer"))
                        ),
                        pathParameters(
                                parameterWithName("meetingId").description("회의 ID")
                        ),
                        responseFields(
                                fieldWithPath("teamId").type(JsonFieldType.NUMBER).description("회의를 주관하는 팀 ID"),
                                fieldWithPath("teamName").type(JsonFieldType.STRING).description("회의를 주관하는 팀 명")
                        )
                ));
    }
}
