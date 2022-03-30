package com.cmc.meeron.meeting.adapter.in;

import com.cmc.meeron.common.exception.ClientErrorCode;
import com.cmc.meeron.meeting.adapter.in.request.FindMeetingAttendeesParameters;
import com.cmc.meeron.meeting.adapter.in.request.FindMeetingAttendeesParametersBuilder;
import com.cmc.meeron.meeting.application.port.in.response.MeetingAttendeesResponseDto;
import com.cmc.meeron.meeting.application.port.in.response.MeetingAttendeesResponseDtoBuilder;
import com.cmc.meeron.support.restdocs.RestDocsTestSupport;
import com.cmc.meeron.support.security.WithMockJwt;
import com.google.common.net.HttpHeaders;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static com.cmc.meeron.config.RestDocsConfig.field;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockJwt
class AttendeeRestControllerTest extends RestDocsTestSupport {

    @DisplayName("회의 참가자 조회 - 성공")
    @Test
    void get_meeting_attendees_success() throws Exception {

        // given
        FindMeetingAttendeesParameters parameters = FindMeetingAttendeesParametersBuilder.build();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("teamId", parameters.getTeamId().toString());
        MeetingAttendeesResponseDto responseDto = MeetingAttendeesResponseDtoBuilder.build();
        when(attendeeQueryUseCase.getMeetingAttendees(any()))
                .thenReturn(responseDto);

        // given, when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/meetings/{meetingId}/attendees", "1")
                .params(params)
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.attends", hasSize(responseDto.getAttends().size())))
                .andExpect(jsonPath("$.absents", hasSize(responseDto.getAbsents().size())))
                .andExpect(jsonPath("$.unknowns", hasSize(responseDto.getUnknowns().size())))
                .andDo(restDocumentationResultHandler.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access Token").attributes(field("constraints", "JWT Access Token With Bearer"))
                        ),
                        pathParameters(
                                parameterWithName("meetingId").description("참가자를 찾을 회의 ID")
                        ),
                        requestParameters(
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

    @DisplayName("회의 참가자 조회 - 실패 / 제약조건을 지키지 않을 경우")
    @Test
    void get_meeting_attendees_fail_invalid() throws Exception {

        // given, when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/meetings/{meetingId}/attendees", "1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.code", is(ClientErrorCode.BIND_EXCEPTION.getCode())))
                .andExpect(jsonPath("$.errors", hasSize(1)));
    }
}