package com.cmc.meeron.meeting.adapter.in;

import com.cmc.meeron.common.exception.ClientErrorCode;
import com.cmc.meeron.common.exception.meeting.MeetingErrorCode;
import com.cmc.meeron.common.exception.meeting.NotMeetingAdminException;
import com.cmc.meeron.common.exception.meeting.NotWorkspacesTeamException;
import com.cmc.meeron.common.exception.team.TeamErrorCode;
import com.cmc.meeron.common.exception.team.TeamNotFoundException;
import com.cmc.meeron.common.exception.workspace.*;
import com.cmc.meeron.meeting.adapter.in.request.CreateMeetingRequest;
import com.cmc.meeron.meeting.adapter.in.request.DeleteMeetingRequest;
import com.cmc.meeron.meeting.adapter.in.request.DeleteMeetingRequestBuilder;
import com.cmc.meeron.support.restdocs.RestDocsTestSupport;
import com.cmc.meeron.support.security.WithMockJwt;
import com.google.common.net.HttpHeaders;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import static com.cmc.meeron.config.RestDocsConfig.field;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockJwt
class MeetingCommandRestControllerTest extends RestDocsTestSupport {

    @DisplayName("?????? ?????? - ?????? / ??????????????? ????????? ?????? ??????")
    @Test
    void create_meeting_fail_not_valid() throws Exception {

        // given, when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/meetings")
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .content(objectMapper.writeValueAsString(CreateMeetingRequest.builder()
                        .meetingAdminIds(Collections.emptyList())
                        .build()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.errors", hasSize(7)))
                .andExpect(jsonPath("$.code", is(ClientErrorCode.BIND_EXCEPTION.getCode())));
    }

    @DisplayName("?????? ?????? - ?????? / ?????????, ?????? ?????? ??????????????? ????????? ?????? ??????")
    @Test
    void create_meeting_fail_not_text_valid() throws Exception {

        // given
        CreateMeetingRequest request = CreateMeetingRequest.builder()
                .workspaceId(1L)
                .meetingDate(LocalDate.now().plusDays(1))
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(11, 0))
                .meetingName("                ")
                .meetingPurpose("?????? ????????? 10????????? ???????????????.")
                .meetingAdminIds(List.of(1L, 2L))
                .operationTeamId(1L)
                .build();

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/meetings")
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.errors", hasSize(2)))
                .andExpect(jsonPath("$.code", is(ClientErrorCode.BIND_EXCEPTION.getCode())));
    }

    @DisplayName("?????? ?????? - ?????? / ???????????? ?????? ???????????? ?????? ??????")
    @Test
    void create_meeting_fail_not_found_operation_team() throws Exception {

        // given
        CreateMeetingRequest request = createCreateMeetingRequest();
        when(meetingCommandUseCase.createMeeting(any(), any()))
                .thenThrow(new TeamNotFoundException());

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/meetings")
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.code", is(TeamErrorCode.NOT_FOUND_TEAM.getCode())));
    }

    private CreateMeetingRequest createCreateMeetingRequest() {
        return CreateMeetingRequest.builder()
                .workspaceId(1L)
                .meetingDate(LocalDate.now().plusDays(3))
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(11, 0))
                .meetingName("????????? ??????")
                .meetingPurpose("????????? ?????? ??????")
                .operationTeamId(1L)
                .meetingAdminIds(List.of(1L, 2L))
                .build();
    }

    @DisplayName("?????? ?????? - ?????? / ????????????????????? ???????????? ?????? ??????")
    @Test
    void create_meeting_fail_not_found_workspace() throws Exception {

        // given
        CreateMeetingRequest request = createCreateMeetingRequest();
        when(meetingCommandUseCase.createMeeting(any(), any()))
                .thenThrow(new WorkspaceNotFoundException());

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/meetings")
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.code", is(WorkspaceErrorCode.NOT_FOUND.getCode())));
    }

    @DisplayName("?????? ?????? - ?????? / ?????? ???????????? ???????????? ?????? ??????")
    @Test
    void create_meeting_fail_not_found_workspace_user_me() throws Exception {

        // given
        CreateMeetingRequest request = createCreateMeetingRequest();
        when(meetingCommandUseCase.createMeeting(any(), any()))
                .thenThrow(new WorkspaceUserNotFoundException());

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/meetings")
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.code", is(WorkspaceUserErrorCode.NOT_FOUND.getCode())));
    }

    @DisplayName("?????? ?????? - ?????? / ???????????? ????????? ????????????????????? ????????? ?????? ??????")
    @Test
    void create_meeting_fail_not_equal_workspace() throws Exception {

        // given
        CreateMeetingRequest request = createCreateMeetingRequest();
        when(meetingCommandUseCase.createMeeting(any(), any()))
                .thenThrow(new WorkspaceUsersNotInEqualWorkspaceException());

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/meetings")
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.code", is(WorkspaceUserErrorCode.NOT_BELONG_TO_WORKSPACE.getCode())));
    }

    @DisplayName("?????? ?????? - ?????? / ?????????????????? ????????? ?????? ?????? ????????????????????? ?????? ??????")
    @Test
    void create_meeting_fail_not_equal_team_and_workspace() throws Exception {

        // given
        CreateMeetingRequest request = createCreateMeetingRequest();
        when(meetingCommandUseCase.createMeeting(any(), any()))
                .thenThrow(new NotWorkspacesTeamException());

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/meetings")
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.code", is(MeetingErrorCode.OPERATION_TEAM_IS_NOT_IN_WORKSPACE.getCode())));
    }

    @DisplayName("?????? ?????? - ??????")
    @Test
    void create_meeting_success() throws Exception {

        // given
        CreateMeetingRequest request = createCreateMeetingRequest();
        when(meetingCommandUseCase.createMeeting(any(), any()))
                .thenReturn(1L);

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/meetings")
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.meetingId", is(1)))
                .andDo(restDocumentationResultHandler.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access Token").attributes(field("constraints", "JWT Access Token With Bearer"))
                        ),
                        requestFields(
                                fieldWithPath("workspaceId").type(JsonFieldType.NUMBER).description("????????? ?????? ?????????????????? ID"),
                                fieldWithPath("meetingDate").type(JsonFieldType.STRING).description("?????? ?????? ??????").attributes(field("constraints", "'yyyy/M/d' ???????????? ??? ???")),
                                fieldWithPath("startTime").type(JsonFieldType.STRING).description("?????? ?????? ??????").attributes(field("constraints", "'hh:mm a' ???????????? ??? ???")),
                                fieldWithPath("endTime").type(JsonFieldType.STRING).description("?????? ?????? ??????").attributes(field("constraints", "'hh:mm a' ???????????? ??? ???")),
                                fieldWithPath("meetingName").type(JsonFieldType.STRING).description("?????????").attributes(field("constraints", "1??? ?????? 30??? ????????? ??? ???, ???????????? ????????? ??????")),
                                fieldWithPath("meetingPurpose").type(JsonFieldType.STRING).description("?????? ??????").attributes(field("constraints", "1??? ?????? 10??? ????????? ??? ???, ???????????? ????????? ??????")),
                                fieldWithPath("operationTeamId").type(JsonFieldType.NUMBER).description("???????????? ??? ID"),
                                fieldWithPath("meetingAdminIds[]").type(JsonFieldType.ARRAY).optional().description("?????? ?????? ???????????? ????????? Workspace User ID").attributes(field("constraints", "????????? Workspace User Id??? ????????? ????????????????????? ????????? ??????."))
                        ),
                        responseFields(
                                fieldWithPath("meetingId").type(JsonFieldType.NUMBER).description("????????? ?????? ID")
                        )
                ));
    }

    @DisplayName("?????? ?????? - ??????")
    @Test
    void delete_meeting_success() throws Exception {

        // given
        DeleteMeetingRequest request = DeleteMeetingRequestBuilder.build();

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/meetings/{meetingId}/delete", "1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(restDocumentationResultHandler.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access Token").attributes(field("constraints", "JWT Access Token With Bearer"))
                        ),
                        pathParameters(
                                parameterWithName("meetingId").description("?????? ?????? ID")
                        ),
                        requestFields(
                                fieldWithPath("attendeeWorkspaceUserId").description("????????? ????????? ????????? ?????????????????? ?????? ID")
                        )
                ));
    }

    @DisplayName("?????? ?????? - ?????? / ?????? ???????????? ?????? ??????")
    @Test
    void delete_meeting_fail_not_meeting_admin() throws Exception {

        // given
        doThrow(new NotMeetingAdminException())
                .when(meetingCommandUseCase)
                .deleteMeeting(any());
        DeleteMeetingRequest request = DeleteMeetingRequestBuilder.build();

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/meetings/{meetingId}/delete", "1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.code", is(MeetingErrorCode.NOT_MEETING_ADMIN.getCode())));
    }
}
