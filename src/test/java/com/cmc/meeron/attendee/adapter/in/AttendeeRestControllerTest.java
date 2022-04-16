package com.cmc.meeron.attendee.adapter.in;

import com.cmc.meeron.attendee.adapter.in.request.ChangeAttendStatusRequest;
import com.cmc.meeron.attendee.adapter.in.request.ChangeAttendStatusRequestBuilder;
import com.cmc.meeron.attendee.adapter.in.request.JoinAttendeesRequest;
import com.cmc.meeron.attendee.adapter.in.request.JoinAttendeesRequestBuilder;
import com.cmc.meeron.common.exception.ClientErrorCode;
import com.cmc.meeron.common.exception.meeting.AttendeeDuplicateException;
import com.cmc.meeron.common.exception.meeting.AttendeeNotFoundException;
import com.cmc.meeron.common.exception.meeting.MeetingErrorCode;
import com.cmc.meeron.common.exception.meeting.MeetingNotFoundException;
import com.cmc.meeron.common.exception.workspace.WorkspaceErrorCode;
import com.cmc.meeron.common.exception.workspace.WorkspaceNotFoundException;
import com.cmc.meeron.common.exception.workspace.WorkspaceUserErrorCode;
import com.cmc.meeron.common.exception.workspace.WorkspaceUsersNotInEqualWorkspaceException;
import com.cmc.meeron.attendee.application.port.in.response.MeetingAttendeesResponseDto;
import com.cmc.meeron.attendee.application.port.in.response.MeetingAttendeesResponseDtoBuilder;
import com.cmc.meeron.attendee.application.port.in.response.MeetingTeamAttendeesResponseDto;
import com.cmc.meeron.attendee.application.port.in.response.MeetingTeamAttendeesResponseDtoBuilder;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static com.cmc.meeron.config.RestDocsConfig.field;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockJwt
class AttendeeRestControllerTest extends RestDocsTestSupport {

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
                .andExpect(MockMvcResultMatchers.handler().handlerType(AttendeeRestController.class))
                .andDo(restDocumentationResultHandler.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access Token").attributes(field("constraints", "JWT Access Token With Bearer"))
                        ),
                        pathParameters(
                                parameterWithName("workspaceUserId").description("회의 참여 상태를 변경할 워크스페이스 유저 ID")
                        ),
                        requestFields(
                                fieldWithPath("meetingId").type(JsonFieldType.NUMBER).description("회의 ID"),
                                fieldWithPath("status").type(JsonFieldType.STRING).description("변경할 상태").attributes(field("constraints", "'attend', 'absent' 중 하나를 입력해야 함"))
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
                .andExpect(MockMvcResultMatchers.handler().handlerType(AttendeeRestController.class))
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
                .when(attendeeCommandUseCase)
                .changeAttendStatus(any());

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/attendees/{workspaceUserId}",
                "1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.handler().handlerType(AttendeeRestController.class))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.code", is(MeetingErrorCode.NOT_FOUND_MEETING.getCode())));
    }

    @DisplayName("참가자의 상태 변경 - 실패 / 회의 참가자를 찾을 수 없을 경우")
    @Test
    void change_attendee_status_fail_not_found_attendee() throws Exception {

        // given
        ChangeAttendStatusRequest request = ChangeAttendStatusRequestBuilder.build();
        doThrow(new AttendeeNotFoundException())
                .when(attendeeCommandUseCase)
                .changeAttendStatus(any());

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/attendees/{workspaceUserId}",
                "1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.handler().handlerType(AttendeeRestController.class))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.code", is(MeetingErrorCode.NOT_FOUND_ATTENDEE.getCode())));
    }

    @DisplayName("회의 참가자 추가 - 실패 / 회의 참가자의 워크스페이스 유저 ID를 하나도 주지 않을 경우")
    @Test
    void join_attendees_fail_empty_workspace_user_ids() throws Exception {

        // given
        JoinAttendeesRequest request = JoinAttendeesRequestBuilder.buildInvalid();

        // when, then, docs
        mockMvc.perform(MockMvcRequestBuilders.post("/api/meetings/{meetingId}/attendees", "1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(handler().handlerType(AttendeeRestController.class))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.code", is(ClientErrorCode.BIND_EXCEPTION.getCode())));
    }

    @DisplayName("회의 참가자 추가 - 실패 / 존재하지 않는 회의일 경우")
    @Test
    void join_attendees_fail_not_found_meeting() throws Exception {

        // given
        doThrow(new MeetingNotFoundException())
                .when(attendeeCommandUseCase)
                .joinAttendees(any());
        JoinAttendeesRequest request = JoinAttendeesRequestBuilder.build();

        // when, then, docs
        mockMvc.perform(MockMvcRequestBuilders.post("/api/meetings/{meetingId}/attendees", "1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(handler().handlerType(AttendeeRestController.class))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.code", is(MeetingErrorCode.NOT_FOUND_MEETING.getCode())));
    }

    @DisplayName("회의 참가자 추가 - 실패 / 존재하지 않는 워크스페이스일 경우")
    @Test
    void join_attendees_fail_not_found_workspace() throws Exception {

        // given
        doThrow(new WorkspaceNotFoundException())
                .when(attendeeCommandUseCase)
                .joinAttendees(any());
        JoinAttendeesRequest request = JoinAttendeesRequestBuilder.build();

        // when, then, docs
        mockMvc.perform(MockMvcRequestBuilders.post("/api/meetings/{meetingId}/attendees", "1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(handler().handlerType(AttendeeRestController.class))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.code", is(WorkspaceErrorCode.NOT_FOUND.getCode())));
    }

    @DisplayName("회의 참가자 추가 - 실패 / 참가자가 같은 워크스페이스에 속하지 않은 경우")
    @Test
    void join_attendees_fail_not_equal_attendees_workspace() throws Exception {

        // given
        doThrow(new WorkspaceUsersNotInEqualWorkspaceException())
                .when(attendeeCommandUseCase)
                .joinAttendees(any());
        JoinAttendeesRequest request = JoinAttendeesRequestBuilder.build();

        // when, then, docs
        mockMvc.perform(MockMvcRequestBuilders.post("/api/meetings/{meetingId}/attendees", "1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(handler().handlerType(AttendeeRestController.class))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.code", is(WorkspaceUserErrorCode.NOT_BELONG_TO_WORKSPACE.getCode())));
    }

    @DisplayName("회의 참가자 추가 - 실패 / 이미 참여중인 참가자가 있을 경우")
    @Test
    void join_attendees_fail_duplicate_attendees() throws Exception {

        // given
        doThrow(new AttendeeDuplicateException())
                .when(attendeeCommandUseCase)
                .joinAttendees(any());
        JoinAttendeesRequest request = JoinAttendeesRequestBuilder.build();

        // when, then, docs
        mockMvc.perform(MockMvcRequestBuilders.post("/api/meetings/{meetingId}/attendees", "1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(handler().handlerType(AttendeeRestController.class))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.code", is(MeetingErrorCode.ATTENDEE_DUPLICATE.getCode())));
    }

    @DisplayName("회의 참가자 추가 - 성공")
    @Test
    void join_attendees_success() throws Exception {

        // given
        JoinAttendeesRequest request = JoinAttendeesRequestBuilder.build();

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/meetings/{meetingId}/attendees", "1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(handler().handlerType(AttendeeRestController.class))
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
    @DisplayName("회의 상세 조회시 참가자 조회 - 성공")
    @Test
    void get_meeting_attendees_success() throws Exception {

        // given
        List<MeetingAttendeesResponseDto> responseDtos = MeetingAttendeesResponseDtoBuilder.build();
        when(attendeeQueryUseCase.getMeetingAttendees(any()))
                .thenReturn(responseDtos);

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/meetings/{meetingId}/attendees/teams", "1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.attendees[0].teamId", is(responseDtos.get(0).getTeamId().intValue())))
                .andExpect(jsonPath("$.attendees[0].teamName", is(responseDtos.get(0).getTeamName())))
                .andExpect(jsonPath("$.attendees[0].attends", is(responseDtos.get(0).getAttends())))
                .andExpect(jsonPath("$.attendees[0].absents", is(responseDtos.get(0).getAbsents())))
                .andExpect(jsonPath("$.attendees[0].unknowns", is(responseDtos.get(0).getUnknowns())))
                .andExpect(jsonPath("$.attendees[1].teamId", is(responseDtos.get(1).getTeamId().intValue())))
                .andExpect(jsonPath("$.attendees[1].teamName", is(responseDtos.get(1).getTeamName())))
                .andExpect(jsonPath("$.attendees[1].attends", is(responseDtos.get(1).getAttends())))
                .andExpect(jsonPath("$.attendees[1].absents", is(responseDtos.get(1).getAbsents())))
                .andExpect(jsonPath("$.attendees[1].unknowns", is(responseDtos.get(1).getUnknowns())))
                .andExpect(handler().handlerType(AttendeeRestController.class))
                .andDo(restDocumentationResultHandler.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access Token").attributes(field("constraints", "JWT Access Token With Bearer"))
                        ),
                        pathParameters(
                                parameterWithName("meetingId").description("참가자를 찾을 회의 ID")
                        ),
                        responseFields(
                                fieldWithPath("attendees[].teamId").type(JsonFieldType.NUMBER).description("회의 참여자의 팀 ID"),
                                fieldWithPath("attendees[].teamName").type(JsonFieldType.STRING).description("회의 참여자의 팀명"),
                                fieldWithPath("attendees[].attends").type(JsonFieldType.NUMBER).description("회의 참여 확정자 수"),
                                fieldWithPath("attendees[].absents").type(JsonFieldType.NUMBER).description("회의 불참자 수"),
                                fieldWithPath("attendees[].unknowns").type(JsonFieldType.NUMBER).description("회의 참여 미응답자 수")
                        )
                ));
    }

    @DisplayName("회의 참가자 팀별 상세 조회 - 성공")
    @Test
    void get_meeting_team_attendees_success() throws Exception {

        // given
        MeetingTeamAttendeesResponseDto responseDto = MeetingTeamAttendeesResponseDtoBuilder.build();
        when(attendeeQueryUseCase.getMeetingTeamAttendees(any()))
                .thenReturn(responseDto);

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/meetings/{meetingId}/attendees/teams/{teamId}",
                "1", "1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.attends", hasSize(responseDto.getAttends().size())))
                .andExpect(jsonPath("$.absents", hasSize(responseDto.getAbsents().size())))
                .andExpect(jsonPath("$.unknowns", hasSize(responseDto.getUnknowns().size())))
                .andExpect(handler().handlerType(AttendeeRestController.class))
                .andDo(restDocumentationResultHandler.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access Token").attributes(field("constraints", "JWT Access Token With Bearer"))
                        ),
                        pathParameters(
                                parameterWithName("meetingId").description("참가자를 찾을 회의 ID"),
                                parameterWithName("teamId").description("참가자를 찾을 팀 ID")
                        ),
                        responseFields(
                                fieldWithPath("attends[].workspaceUserId").type(JsonFieldType.NUMBER).description("회의 참여자의 워크스페이스 유저 ID"),
                                fieldWithPath("attends[].profileImageUrl").type(JsonFieldType.STRING).description("회의 참여자의 프로필 이미지 URL"),
                                fieldWithPath("attends[].nickname").type(JsonFieldType.STRING).description("회의 참여자의 닉네임"),
                                fieldWithPath("attends[].position").type(JsonFieldType.STRING).description("회의 참여자의 직책"),

                                fieldWithPath("absents[].workspaceUserId").type(JsonFieldType.NUMBER).description("회의 불참자의 워크스페이스 유저 ID"),
                                fieldWithPath("absents[].profileImageUrl").type(JsonFieldType.STRING).description("회의 불참자의 프로필 이미지 URL"),
                                fieldWithPath("absents[].nickname").type(JsonFieldType.STRING).description("회의 불참자의 닉네임"),
                                fieldWithPath("absents[].position").type(JsonFieldType.STRING).description("회의 불참자의 직책"),

                                fieldWithPath("unknowns[].workspaceUserId").type(JsonFieldType.NUMBER).description("회의 참여 미정자의 워크스페이스 유저 ID"),
                                fieldWithPath("unknowns[].profileImageUrl").type(JsonFieldType.STRING).description("회의 참여 미정자의 프로필 이미지 URL"),
                                fieldWithPath("unknowns[].nickname").type(JsonFieldType.STRING).description("회의 참여 미정자의 닉네임"),
                                fieldWithPath("unknowns[].position").type(JsonFieldType.STRING).description("회의 참여 미정자의 직책")
                        )
                ));
    }
}
