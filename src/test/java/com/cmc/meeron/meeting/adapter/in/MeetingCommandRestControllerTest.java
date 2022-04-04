package com.cmc.meeron.meeting.adapter.in;

import com.cmc.meeron.common.exception.ClientErrorCode;
import com.cmc.meeron.common.exception.meeting.*;
import com.cmc.meeron.common.exception.team.TeamErrorCode;
import com.cmc.meeron.common.exception.team.TeamNotFoundException;
import com.cmc.meeron.common.exception.workspace.*;
import com.cmc.meeron.meeting.adapter.in.request.*;
import com.cmc.meeron.support.restdocs.RestDocsTestSupport;
import com.cmc.meeron.support.security.WithMockJwt;
import com.google.common.net.HttpHeaders;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

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

    @DisplayName("회의 생성 - 실패 / 제약조건을 지키지 않을 경우")
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

    @DisplayName("회의 생성 - 실패 / 회의명, 회의 성격 제약조건을 지키지 않을 경우")
    @Test
    void create_meeting_fail_not_text_valid() throws Exception {

        // given
        CreateMeetingRequest request = CreateMeetingRequest.builder()
                .workspaceId(1L)
                .meetingDate(LocalDate.now().plusDays(1))
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(11, 0))
                .meetingName("                ")
                .meetingPurpose("회의 성격은 10글자를 넘어갔단다.")
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

    @DisplayName("회의 생성 - 실패 / 주관하는 팀이 존재하지 않을 경우")
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
                .meetingName("테스트 회의")
                .meetingPurpose("테스트 회의 성격")
                .operationTeamId(1L)
                .meetingAdminIds(List.of(1L, 2L))
                .build();
    }

    @DisplayName("회의 생성 - 실패 / 워크스페이스가 존재하지 않을 경우")
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

    @DisplayName("회의 생성 - 실패 / 회의 작성자가 존재하지 않을 경우")
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

    @DisplayName("회의 생성 - 실패 / 관리자가 선택한 워크스페이스에 속하지 않은 경우")
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

    @DisplayName("회의 생성 - 실패 / 워크스페이스 유저와 팀이 속한 워크스페이스가 다를 경우")
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

    @DisplayName("회의 생성 - 성공")
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
                                fieldWithPath("workspaceId").type(JsonFieldType.NUMBER).description("회의를 만들 워크스페이스 ID"),
                                fieldWithPath("meetingDate").type(JsonFieldType.STRING).description("회의 시작 날짜").attributes(field("constraints", "'yyyy/M/d' 형식으로 줄 것")),
                                fieldWithPath("startTime").type(JsonFieldType.STRING).description("회의 시작 시간").attributes(field("constraints", "'hh:mm a' 형식으로 줄 것")),
                                fieldWithPath("endTime").type(JsonFieldType.STRING).description("회의 종료 시간").attributes(field("constraints", "'hh:mm a' 형식으로 줄 것")),
                                fieldWithPath("meetingName").type(JsonFieldType.STRING).description("회의명").attributes(field("constraints", "1자 이상 30자 이하로 줄 것, 비어있는 문자열 금지")),
                                fieldWithPath("meetingPurpose").type(JsonFieldType.STRING).description("회의 성격").attributes(field("constraints", "1자 이상 10자 이하로 줄 것, 비어있는 문자열 금지")),
                                fieldWithPath("operationTeamId").type(JsonFieldType.NUMBER).description("주관하는 팀 ID"),
                                fieldWithPath("meetingAdminIds[]").type(JsonFieldType.ARRAY).optional().description("회의 공동 관리자로 임명할 Workspace User ID").attributes(field("constraints", "입력한 Workspace User Id는 하나의 워크스페이스에 속해야 한다."))
                        ),
                        responseFields(
                                fieldWithPath("meetingId").type(JsonFieldType.NUMBER).description("생성된 회의 ID")
                        )
                ));
    }

    @DisplayName("회의 참가자 추가 - 실패 / 회의 참가자의 워크스페이스 유저 ID를 하나도 주지 않을 경우")
    @Test
    void join_attendees_fail_empty_workspace_user_ids() throws Exception {

        // given
        JoinAttendeesRequest request = JoinAttendeesRequest.builder().build();

        // when, then, docs
        mockMvc.perform(MockMvcRequestBuilders.post("/api/meetings/{meetingId}/attendees", "1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.code", is(ClientErrorCode.BIND_EXCEPTION.getCode())));
    }

    private JoinAttendeesRequest createJoinAttendeesRequest() {
        return JoinAttendeesRequest.builder()
                .workspaceUserIds(List.of(1L, 2L))
                .build();
    }

    @DisplayName("회의 참가자 추가 - 실패 / 존재하지 않는 회의일 경우")
    @Test
    void join_attendees_fail_not_found_meeting() throws Exception {

        // given
        doThrow(new MeetingNotFoundException())
                .when(meetingCommandUseCase)
                .joinAttendees(any());
        JoinAttendeesRequest request = createJoinAttendeesRequest();

        // when, then, docs
        mockMvc.perform(MockMvcRequestBuilders.post("/api/meetings/{meetingId}/attendees", "1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.code", is(MeetingErrorCode.NOT_FOUND_MEETING.getCode())));
    }

    @DisplayName("회의 참가자 추가 - 실패 / 존재하지 않는 워크스페이스일 경우")
    @Test
    void join_attendees_fail_not_found_workspace() throws Exception {

        // given
        doThrow(new WorkspaceNotFoundException())
                .when(meetingCommandUseCase)
                .joinAttendees(any());
        JoinAttendeesRequest request = createJoinAttendeesRequest();

        // when, then, docs
        mockMvc.perform(MockMvcRequestBuilders.post("/api/meetings/{meetingId}/attendees", "1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.code", is(WorkspaceErrorCode.NOT_FOUND.getCode())));
    }

    @DisplayName("회의 참가자 추가 - 실패 / 참가자가 같은 워크스페이스에 속하지 않은 경우")
    @Test
    void join_attendees_fail_not_equal_attendees_workspace() throws Exception {

        // given
        doThrow(new WorkspaceUsersNotInEqualWorkspaceException())
                .when(meetingCommandUseCase)
                .joinAttendees(any());
        JoinAttendeesRequest request = createJoinAttendeesRequest();

        // when, then, docs
        mockMvc.perform(MockMvcRequestBuilders.post("/api/meetings/{meetingId}/attendees", "1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.code", is(WorkspaceUserErrorCode.NOT_BELONG_TO_WORKSPACE.getCode())));
    }

    @DisplayName("회의 참가자 추가 - 실패 / 이미 참여중인 참가자가 있을 경우")
    @Test
    void join_attendees_fail_duplicate_attendees() throws Exception {

        // given
        doThrow(new AttendeeDuplicateException())
                .when(meetingCommandUseCase)
                .joinAttendees(any());
        JoinAttendeesRequest request = createJoinAttendeesRequest();

        // when, then, docs
        mockMvc.perform(MockMvcRequestBuilders.post("/api/meetings/{meetingId}/attendees", "1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.code", is(MeetingErrorCode.ATTENDEE_DUPLICATE.getCode())));
    }

    @DisplayName("회의 참가자 추가 - 성공")
    @Test
    void join_attendees_success() throws Exception {

        // given
        JoinAttendeesRequest request = createJoinAttendeesRequest();

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/meetings/{meetingId}/attendees", "1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(restDocumentationResultHandler.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access Token").attributes(field("constraints", "JWT Access Token With Bearer"))
                        ),
                        pathParameters(
                                parameterWithName("meetingId").description("참가자를 참여시킬 회의 ID")
                        ),
                        requestFields(
                                fieldWithPath("workspaceUserIds[]").type(JsonFieldType.ARRAY).description("참여할 워크스페이스 유저의 ID들").attributes(field("constraints", "반드시 1개 이상 줄 것"))
                        )
                ));
    }

    @DisplayName("아젠다 생성 - 실패 / 아젠다 명이 48자 이상 넘어갈 경우")
    @Test
    void create_agenda_fail_exceeded_agenda_name_over_48() throws Exception {

        // given
        CreateAgendaRequest request = CreateAgendaRequest.builder()
                .agendas(List.of(
                        CreateAgendaRequest.AgendaRequest.builder()
                                .order(1)
                                .name("해당 아젠다 명은 48자가 넘어갑니다. 해당 아젠다 명은 48자가 넘어갑니다. 해당 아젠다 명은 48자가 넘어갑니다. 해당 아젠다 명은 48자가 넘어갑니다. 해당 아젠다 명은 48자가 넘어갑니다. 해당 아젠다 명은 48자가 넘어갑니다.")
                                .issues(List.of(CreateAgendaRequest.IssueRequest.builder()
                                        .issue("테스트이슈")
                                        .build()))
                                .build())).build();

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/meetings/{meetingId}/agendas", "1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.code", is(ClientErrorCode.BIND_EXCEPTION.getCode())));
    }

    @DisplayName("아젠다 생성 - 실패 / 아젠다가 5개 초과일 경우")
    @Test
    void create_agenda_fail_exceeded_agenda_over_5() throws Exception {

        // given
        CreateAgendaRequest request = CreateAgendaRequest.builder()
                .agendas(List.of(
                        CreateAgendaRequest.AgendaRequest.builder()
                                .order(1)
                                .name("테스트 아젠다")
                                .issues(List.of(CreateAgendaRequest.IssueRequest.builder()
                                        .issue("테스트이슈")
                                        .build()))
                                .build(),
                        CreateAgendaRequest.AgendaRequest.builder()
                                .order(2)
                                .name("테스트 아젠다")
                                .issues(List.of(CreateAgendaRequest.IssueRequest.builder()
                                        .issue("테스트이슈")
                                        .build()))
                                .build(),
                        CreateAgendaRequest.AgendaRequest.builder()
                                .order(3)
                                .name("테스트 아젠다")
                                .issues(List.of(CreateAgendaRequest.IssueRequest.builder()
                                        .issue("테스트이슈")
                                        .build()))
                                .build(),
                        CreateAgendaRequest.AgendaRequest.builder()
                                .order(4)
                                .name("테스트 아젠다")
                                .issues(List.of(CreateAgendaRequest.IssueRequest.builder()
                                        .issue("테스트이슈")
                                        .build()))
                                .build(),
                        CreateAgendaRequest.AgendaRequest.builder()
                                .order(5)
                                .name("테스트 아젠다")
                                .issues(List.of(CreateAgendaRequest.IssueRequest.builder()
                                        .issue("테스트이슈")
                                        .build()))
                                .build(),
                        CreateAgendaRequest.AgendaRequest.builder()
                                .order(6)
                                .name("테스트 아젠다")
                                .issues(List.of(CreateAgendaRequest.IssueRequest.builder()
                                        .issue("테스트이슈")
                                        .build()))
                                .build()
                        )).build();

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/meetings/{meetingId}/agendas", "1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.code", is(ClientErrorCode.BIND_EXCEPTION.getCode())));
    }

    @DisplayName("아젠다 생성 - 실패 / 회의가 존재하지 않을 경우")
    @Test
    void create_agenda_fail_not_found_meeting() throws Exception {

        // given
        CreateAgendaRequest request = createCreateAgendasRequest();
        when(meetingCommandUseCase.createAgendas(any()))
                .thenThrow(new MeetingNotFoundException());

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/meetings/{meetingId}/agendas", "1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.code", is(MeetingErrorCode.NOT_FOUND_MEETING.getCode())));
    }

    private CreateAgendaRequest createCreateAgendasRequest() {
        return CreateAgendaRequest.builder()
                .agendas(List.of(
                        CreateAgendaRequest.AgendaRequest.builder()
                                .order(1)
                                .name("테스트 아젠다")
                                .issues(List.of(CreateAgendaRequest.IssueRequest.builder()
                                        .issue("테스트이슈")
                                        .build()))
                                .build()
                ))
                .build();
    }

    @DisplayName("아젠다 생성 - 성공")
    @Test
    void create_agenda_success() throws Exception {

        // given
        CreateAgendaRequest request = createCreateAgendasRequest();
        List<Long> createdAgendaIds = List.of(3L);
        when(meetingCommandUseCase.createAgendas(any()))
                .thenReturn(createdAgendaIds);

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/meetings/{meetingId}/agendas", "1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.createdAgendaIds[0]", is(createdAgendaIds.get(0).intValue())))
                .andDo(restDocumentationResultHandler.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access Token").attributes(field("constraints", "JWT Access Token With Bearer"))
                        ),
                        pathParameters(
                                parameterWithName("meetingId").description("아젠다를 생성할 회의 ID")
                        ),
                        requestFields(
                                fieldWithPath("agendas[].order").type(JsonFieldType.NUMBER).description("아젠다 순서"),
                                fieldWithPath("agendas[].name").type(JsonFieldType.STRING).description("아젠다 제목").attributes(field("constraints", "아젠다 제목은 48자 이내여야 함")),
                                fieldWithPath("agendas[].issues[].issue").type(JsonFieldType.STRING).description("생성할 아젠다의 이슈")
                        ),
                        responseFields(
                                fieldWithPath("createdAgendaIds[]").type(JsonFieldType.ARRAY).description("생성된 아젠다 ID들")
                        )
                ));
    }

    @DisplayName("참가자의 상태 변경 - 성공")
    @Test
    void change_attendee_status_success() throws Exception {

        // given
        ChangeAttendStatusRequest request = ChangeAttendStatusRequestBuilder.build();

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/attendees/{workspaceUserId}",
                "1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(restDocumentationResultHandler.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access Token").attributes(field("constraints", "JWT Access Token With Bearer"))
                        ),
                        pathParameters(
                                parameterWithName("workspaceUserId").description("회의 참여 상태를 변경할 워크스페이스 유저 ID")
                        ),
                        requestFields(
                                fieldWithPath("meetingId").type(JsonFieldType.NUMBER).description("회의 ID"),
                                fieldWithPath("status").type(JsonFieldType.STRING).description("변경할 상태").attributes(field("constraints", "'attend', 'accident', 'absent' 중 하나를 입력해야 함"))
                        )
                ));
    }

    @DisplayName("참가자의 상태 변경 - 실패 / 제약조건을 지키지 않은 경우")
    @Test
    void change_attendee_status_fail_not_valid() throws Exception {

        // given
        ChangeAttendStatusRequest request = ChangeAttendStatusRequestBuilder.buildNotValid();

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/attendees/{workspaceUserId}",
                "1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.code", is(ClientErrorCode.BIND_EXCEPTION.getCode())))
                .andExpect(jsonPath("$.errors", hasSize(2)));
    }

    @DisplayName("참가자의 상태 변경 - 실패 / 회의가 존재하지 않을 경우")
    @Test
    void change_attendee_status_fail_not_found_meeting() throws Exception {

        // given
        ChangeAttendStatusRequest request = ChangeAttendStatusRequestBuilder.build();
        doThrow(new MeetingNotFoundException())
                .when(meetingCommandUseCase)
                .changeAttendStatus(any());

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/attendees/{workspaceUserId}",
                "1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.code", is(MeetingErrorCode.NOT_FOUND_MEETING.getCode())));
    }

    @DisplayName("참가자의 상태 변경 - 실패 / 회의 참가자를 찾을 수 없을 경우")
    @Test
    void change_attendee_status_fail_not_found_attendee() throws Exception {

        // given
        ChangeAttendStatusRequest request = ChangeAttendStatusRequestBuilder.build();
        doThrow(new AttendeeNotFoundException())
                .when(meetingCommandUseCase)
                .changeAttendStatus(any());

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/attendees/{workspaceUserId}",
                "1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.code", is(MeetingErrorCode.NOT_FOUND_ATTENDEE.getCode())));
    }

    @DisplayName("회의 삭제 - 성공")
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
                                parameterWithName("meetingId").description("지울 회의 ID")
                        ),
                        requestFields(
                                fieldWithPath("attendeeWorkspaceUserId").description("회의를 삭제할 유저의 워크스페이스 유저 ID")
                        )
                ));
    }

    @DisplayName("회의 삭제 - 실패 / 회의 관리자가 아닌 경우")
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
