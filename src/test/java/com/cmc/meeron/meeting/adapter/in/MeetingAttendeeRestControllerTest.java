package com.cmc.meeron.meeting.adapter.in;

import com.cmc.meeron.meeting.application.port.in.response.MeetingAttendeesResponseDto;
import com.cmc.meeron.meeting.application.port.in.response.MeetingAttendeesResponseDtoBuilder;
import com.cmc.meeron.meeting.application.port.in.response.MeetingTeamAttendeesResponseDto;
import com.cmc.meeron.meeting.application.port.in.response.MeetingTeamAttendeesResponseDtoBuilder;
import com.cmc.meeron.support.restdocs.RestDocsTestSupport;
import com.cmc.meeron.support.security.WithMockJwt;
import com.google.common.net.HttpHeaders;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.List;

import static com.cmc.meeron.config.RestDocsConfig.field;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
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
class MeetingAttendeeRestControllerTest extends RestDocsTestSupport {

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