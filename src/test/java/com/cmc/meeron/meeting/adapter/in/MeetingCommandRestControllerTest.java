package com.cmc.meeron.meeting.adapter.in;

import com.cmc.meeron.common.exception.meeting.MeetingNotFoundException;
import com.cmc.meeron.common.exception.meeting.NotWorkspacesTeamException;
import com.cmc.meeron.common.exception.team.TeamNotFoundException;
import com.cmc.meeron.common.exception.workspace.WorkspaceUsersNotInEqualWorkspaceException;
import com.cmc.meeron.meeting.adapter.in.request.CreateMeetingRequest;
import com.cmc.meeron.meeting.adapter.in.request.JoinAttendeesRequest;
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
import java.util.List;

import static com.cmc.meeron.config.RestDocsConfig.field;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
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
                .content(objectMapper.writeValueAsString(CreateMeetingRequest.builder().build()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.errors", hasSize(6)))
                .andExpect(jsonPath("$.code", is("MEERON-400")));
    }

    @DisplayName("회의 생성 - 실패 / 회의명, 회의 성격 제약조건을 지키지 않을 경우")
    @Test
    void create_meeting_fail_not_text_valid() throws Exception {

        // given
        CreateMeetingRequest request = CreateMeetingRequest.builder()
                .meetingDate(LocalDate.now().plusDays(1))
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(11, 0))
                .meetingName("테")
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
                .andExpect(jsonPath("$.code", is("MEERON-400")));
    }

    @DisplayName("회의 생성 - 실패 / 주관하는 팀이 존재하지 않을 경우")
    @Test
    void create_meeting_fail_not_found_operation_team() throws Exception {

        // given
        CreateMeetingRequest request = createCreateMeetingRequest();
        when(meetingCommandUseCase.creteMeeting(any()))
                .thenThrow(new TeamNotFoundException());

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/meetings")
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.code", is("MEERON-400")));
    }

    private CreateMeetingRequest createCreateMeetingRequest() {
        return CreateMeetingRequest.builder()
                .meetingDate(LocalDate.now().plusDays(3))
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(11, 0))
                .meetingName("테스트 회의")
                .meetingPurpose("테스트 회의 성격")
                .operationTeamId(1L)
                .meetingAdminIds(List.of(1L, 2L))
                .build();
    }

    @DisplayName("회의 생성 - 실패 / 회의 공동 관리자가 하나의 워크스페이스에 속하지 않은 경우")
    @Test
    void create_meeting_fail_not_equal_workspace() throws Exception {

        // given
        CreateMeetingRequest request = createCreateMeetingRequest();
        when(meetingCommandUseCase.creteMeeting(any()))
                .thenThrow(new WorkspaceUsersNotInEqualWorkspaceException());

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/meetings")
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.code", is("MEERON-400")));
    }

    @DisplayName("회의 생성 - 실패 / 현재 시간보다 이전 시간에 회의를 시작할 경우")
    @Test
    void create_meeting_fail_not_before_start_time_at_now() throws Exception {

        // given
        CreateMeetingRequest request = CreateMeetingRequest.builder()
                .meetingDate(LocalDate.now())
                .startTime(LocalTime.of(0, 1))
                .endTime(LocalTime.of(2, 0))
                .meetingName("테스트 회의")
                .meetingPurpose("테스트 회의 성격")
                .operationTeamId(1L)
                .meetingAdminIds(List.of(1L, 2L))
                .build();

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/meetings")
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.errors", hasSize(2)))
                .andExpect(jsonPath("$.code", is("MEERON-400")));
    }

    @DisplayName("회의 생성 - 실패 / 워크스페이스 유저와 팀이 속한 워크스페이스가 다를 경우")
    @Test
    void create_meeting_fail_not_equal_team_admins_workspace() throws Exception {

        // given
        CreateMeetingRequest request = createCreateMeetingRequest();
        when(meetingCommandUseCase.creteMeeting(any()))
                .thenThrow(new NotWorkspacesTeamException());

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/meetings")
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.code", is("MEERON-400")));
    }

    @DisplayName("회의 생성 - 성공")
    @Test
    void create_meeting_success() throws Exception {

        // given
        CreateMeetingRequest request = createCreateMeetingRequest();
        when(meetingCommandUseCase.creteMeeting(any()))
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
                                fieldWithPath("meetingDate").type(JsonFieldType.STRING).description("회의 시작 날짜").attributes(field("constraints", "오늘 혹은 그 이후 날짜 'yyyy-MM-dd'로 줄 것")),
                                fieldWithPath("startTime").type(JsonFieldType.STRING).description("회의 시작 시간").attributes(field("constraints", "'HH:mm' 형식으로 줄 것")),
                                fieldWithPath("endTime").type(JsonFieldType.STRING).description("회의 종료 시간").attributes(field("constraints", "'HH:mm' 형식으로 줄 것")),
                                fieldWithPath("meetingName").type(JsonFieldType.STRING).description("회의명").attributes(field("constraints", "3자 이상 30자 이하로 줄 것")),
                                fieldWithPath("meetingPurpose").type(JsonFieldType.STRING).description("회의 성격").attributes(field("constraints", "1자 이상 10자 이하로 줄 것")),
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
                .andExpect(jsonPath("$.code", is("MEERON-400")));
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
                .andExpect(jsonPath("$.code", is("MEERON-400")));
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
                .andExpect(jsonPath("$.code", is("MEERON-400")));
    }

    @DisplayName("회의 참가자 추가 - 실패 / 참가자가 같은 워크스페이스에 속해도 생성된 회의의 워크스페이스 정보와 일치하지 않는 경우")
    @Test
    void join_attendees_fail_not_equal_workspace() throws Exception {

        // given
        doThrow(new NotWorkspacesTeamException())
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
                .andExpect(jsonPath("$.code", is("MEERON-400")));
    }

    @DisplayName("회의 참가자 추가 - 성공")
    @Test
    void join_attendees_success() throws Exception {

        // given
        JoinAttendeesRequest request = createJoinAttendeesRequest();

        // when, then, docs
        mockMvc.perform(MockMvcRequestBuilders.post("/api/meetings/{meetingId}/attendees", "1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(restDocumentationResultHandler.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access Token").attributes(field("constraints", "JWT Access Token With Bearer"))
                        ),
                        requestFields(
                                fieldWithPath("workspaceUserIds[]").type(JsonFieldType.ARRAY).description("참여할 워크스페이스 유저의 ID들").attributes(field("constraints", "반드시 1개 이상 줄 것"))
                        )
                ));
    }
}
