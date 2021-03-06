package com.cmc.meeron.team.adapter.in;

import com.cmc.meeron.common.exception.ClientErrorCode;
import com.cmc.meeron.common.exception.team.TeamCountsConditionException;
import com.cmc.meeron.common.exception.team.TeamErrorCode;
import com.cmc.meeron.common.exception.team.TeamNotFoundException;
import com.cmc.meeron.support.TestImproved;
import com.cmc.meeron.common.exception.workspace.NotAllFoundWorkspaceUsersException;
import com.cmc.meeron.common.exception.workspace.WorkspaceUserErrorCode;
import com.cmc.meeron.common.exception.workspace.WorkspaceUserNotFoundException;
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
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doThrow;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockJwt
class TeamRestControllerTest extends RestDocsTestSupport {

    @Deprecated
    @DisplayName("?????????????????? ??? ??? ?????? - ??????")
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
                                parameterWithName("workspaceId").description("?????????????????? ID")
                        ),
                        responseFields(
                                fieldWithPath("teams[].teamId").type(JsonFieldType.NUMBER).description("?????????????????? ?????? ??? ID"),
                                fieldWithPath("teams[].teamName").type(JsonFieldType.STRING).description("?????????????????? ?????? ??? ???")
                        )
                ));
    }

    @TestImproved(originMethod = "get_workspace_teams_success")
    @DisplayName("?????????????????? ??? ??? ?????? - ??????")
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
                                parameterWithName("workspaceId").description("?????????????????? ID")
                        ),
                        responseFields(
                                fieldWithPath("teams[].teamId").type(JsonFieldType.NUMBER).description("?????????????????? ?????? ??? ID"),
                                fieldWithPath("teams[].teamName").type(JsonFieldType.STRING).description("?????????????????? ?????? ??? ???")
                        )
                ));
    }

    @DisplayName("??? ?????? - ??????")
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
                                fieldWithPath("workspaceId").type(JsonFieldType.NUMBER).description("?????? ????????? ?????????????????? ID"),
                                fieldWithPath("teamName").type(JsonFieldType.STRING).description("????????? ??????").attributes(field("constraints", "1??? ?????? 10??? ????????? ??????"))
                        ),
                        responseFields(
                                fieldWithPath("createdTeamId").type(JsonFieldType.NUMBER).description("????????? ??? ID")
                        )
                ));
    }

    @DisplayName("??? ?????? - ?????? / ?????? ???????????? ???????????? ??????")
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
                .teamName("?????????")
                .workspaceId(1L)
                .build();
    }

    @DisplayName("??? ?????? - ?????? / ??????????????? ????????? ?????? ??????")
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

    @DisplayName("??? ?????? - ?????? / ??????????????? ????????? ?????? ??????")
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

    @DisplayName("??? ?????? - ??????")
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
                                parameterWithName("teamId").description("????????? ??? ID")
                        ),
                        requestFields(
                                fieldWithPath("adminWorkspaceUserId").type(JsonFieldType.NUMBER).description("????????? ?????????????????? ID (?????? ?????????)")
                        )
                ));

    }

    @DisplayName("?????? ?????? - ?????? / ??????????????? ????????? ?????? ??????")
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

    @DisplayName("?????? ?????? - ??????")
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
                                parameterWithName("teamId").description("????????? ????????? ??? ID")
                        ),
                        requestFields(
                                fieldWithPath("adminWorkspaceUserId").type(JsonFieldType.NUMBER).description("????????? ?????????????????? ID (?????? ?????????)"),
                                fieldWithPath("teamName").type(JsonFieldType.STRING).description("????????? ??????")
                        )
                ));
    }

    @DisplayName("????????? ???????????? ??? ?????? - ?????? / ?????? ?????? ??????")
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

    @DisplayName("????????? ???????????? ??? ?????? -??????")
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
                                parameterWithName("meetingId").description("?????? ID")
                        ),
                        responseFields(
                                fieldWithPath("teamId").type(JsonFieldType.NUMBER).description("????????? ???????????? ??? ID"),
                                fieldWithPath("teamName").type(JsonFieldType.STRING).description("????????? ???????????? ??? ???")
                        )
                ));
    }

    @DisplayName("?????? ?????? - ?????? / ??????????????? ????????? ?????? ??????")
    @Test
    void join_team_members_fail_not_valid() throws Exception {

        // given
        JoinTeamMembersRequest request = JoinTeamMembersRequestBuilder.buildNotValid();

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/teams/{teamId}/join", "1")
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
    void join_team_members_fail_not_found_team() throws Exception {

        // given
        JoinTeamMembersRequest request = JoinTeamMembersRequestBuilder.build();
        doThrow(new TeamNotFoundException())
                .when(teamMemberManageUseCase)
                .joinTeamMembers(any());

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/teams/{teamId}/join", "1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer testAccessToken")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.code", is(TeamErrorCode.NOT_FOUND_TEAM.getCode())));
    }

    @DisplayName("?????? ?????? - ?????? / ?????? ???????????? ?????? ?????? ?????? ?????? ??????")
    @Test
    void join_team_members_fail_invalid_find_workspace_users_count() throws Exception {

        // given
        JoinTeamMembersRequest request = JoinTeamMembersRequestBuilder.build();
        doThrow(new NotAllFoundWorkspaceUsersException())
                .when(teamMemberManageUseCase)
                .joinTeamMembers(any());

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/teams/{teamId}/join", "1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer testAccessToken")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.code", is(WorkspaceUserErrorCode.NOT_ALL_FOUND.getCode())));
    }

    @DisplayName("?????? ?????? - ??????")
    @Test
    void join_team_members_success() throws Exception {

        // given
        JoinTeamMembersRequest request = JoinTeamMembersRequestBuilder.build();

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/teams/{teamId}/join", "1")
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
    void eject_team_member_fail_invalid() throws Exception {

        // given
        EjectTeamMemberRequestV2 request = EjectTeamMemberRequestBuilder.buildInvalidV2();

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/teams/{teamId}/eject", "1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer testAccessToken")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.code", is(ClientErrorCode.BIND_EXCEPTION.getCode())))
                .andExpect(jsonPath("$.errors", hasSize(2)));
    }

    @DisplayName("????????? ?????? - ?????? / ????????? ?????? ?????? ??????")
    @Test
    void eject_team_member_fail_not_found_workspace_user() throws Exception {

        // given
        EjectTeamMemberRequestV2 request = EjectTeamMemberRequestBuilder.buildV2();
        doThrow(new WorkspaceUserNotFoundException())
                .when(teamMemberManageUseCase)
                .ejectTeamMember(any());

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/teams/{teamId}/eject", "1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer testAccessToken")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.code", is(WorkspaceUserErrorCode.NOT_FOUND.getCode())));
    }

    @DisplayName("????????? ?????? / ??????")
    @Test
    void eject_team_member_success() throws Exception {

        // given
        EjectTeamMemberRequestV2 request = EjectTeamMemberRequestBuilder.buildV2();

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/teams/{teamId}/eject", "1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer testAccessToken")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(restDocumentationResultHandler.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access Token").attributes(field("constraints", "JWT Access Token With Bearer"))
                        ),
                        pathParameters(
                                parameterWithName("teamId").description("????????? ???????????? ID")
                        ),
                        requestFields(
                                fieldWithPath("adminWorkspaceUserId").type(JsonFieldType.NUMBER).description("????????? ?????????????????? ID (?????? ?????????)"),
                                fieldWithPath("ejectWorkspaceUserId").type(JsonFieldType.NUMBER).description("???????????? ?????????????????? ?????? ID")
                        )
                ));
    }
}
