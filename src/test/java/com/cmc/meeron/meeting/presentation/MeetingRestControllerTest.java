package com.cmc.meeron.meeting.presentation;

import com.cmc.meeron.meeting.application.dto.response.TodayMeetingResponseDto;
import com.cmc.meeron.support.restdocs.RestDocsTestSupport;
import com.cmc.meeron.support.security.WithMockJwt;
import com.cmc.meeron.util.LocalDateTimeUtil;
import com.google.common.net.HttpHeaders;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static com.cmc.meeron.config.RestDocsConfig.field;
import static org.hamcrest.Matchers.*;
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
class MeetingRestControllerTest extends RestDocsTestSupport {

    @DisplayName("오늘 회의 리스트 조회 - 성공")
    @Test
    void today_meetings_list_success() throws Exception {

        // given
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("workspaceId", "1");
        params.add("workspaceUserId", "1");
        List<TodayMeetingResponseDto> responseDto = createTodayMeetingResponseDto();
        when(meetingUseCase.getTodayMeetings(any()))
                .thenReturn(responseDto);

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/meetings/today")
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .params(params))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meetings", hasSize(2)))
                .andExpect(jsonPath("$.meetings[0].meetingId", is(responseDto.get(0).getMeetingId().intValue())))
                .andExpect(jsonPath("$.meetings[0].meetingName", is(responseDto.get(0).getMeetingName())))
                .andExpect(jsonPath("$.meetings[0].meetingDate", is(responseDto.get(0).getMeetingDate().toString())))
                .andExpect(jsonPath("$.meetings[0].startTime", is(LocalDateTimeUtil.toStringTime(responseDto.get(0).getStartTime()))))
                .andExpect(jsonPath("$.meetings[0].endTime", is(LocalDateTimeUtil.toStringTime(responseDto.get(0).getEndTime()))))
                .andExpect(jsonPath("$.meetings[0].operationTeamId", is(responseDto.get(0).getOperationTeamId().intValue())))
                .andExpect(jsonPath("$.meetings[0].operationTeamName", is(responseDto.get(0).getOperationTeamName())))
                .andExpect(jsonPath("$.meetings[0].meetingStatus", is(responseDto.get(0).getMeetingStatus())))
                .andExpect(jsonPath("$.meetings[1].meetingId", is(responseDto.get(1).getMeetingId().intValue())))
                .andExpect(jsonPath("$.meetings[1].meetingName", is(responseDto.get(1).getMeetingName())))
                .andExpect(jsonPath("$.meetings[1].meetingDate", is(responseDto.get(1).getMeetingDate().toString())))
                .andExpect(jsonPath("$.meetings[1].startTime", is(LocalDateTimeUtil.toStringTime(responseDto.get(1).getStartTime()))))
                .andExpect(jsonPath("$.meetings[1].endTime", is(LocalDateTimeUtil.toStringTime(responseDto.get(1).getEndTime()))))
                .andExpect(jsonPath("$.meetings[1].operationTeamId", is(responseDto.get(1).getOperationTeamId().intValue())))
                .andExpect(jsonPath("$.meetings[1].operationTeamName", is(responseDto.get(1).getOperationTeamName())))
                .andExpect(jsonPath("$.meetings[1].meetingStatus", is(responseDto.get(1).getMeetingStatus())))
                .andDo(restDocumentationResultHandler.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access Token").attributes(field("constraints", "JWT Access Token With Bearer"))
                        ),
                        requestParameters(
                                parameterWithName("workspaceId").description("워크스페이스 ID"),
                                parameterWithName("workspaceUserId").description("워크스페이스 유저 ID")
                        ),
                        responseFields(
                                fieldWithPath("meetings[].meetingId").type(JsonFieldType.NUMBER).description("회의 ID"),
                                fieldWithPath("meetings[].meetingName").type(JsonFieldType.STRING).description("회의 명"),
                                fieldWithPath("meetings[].meetingDate").type(JsonFieldType.STRING).description("회의 진행 날짜"),
                                fieldWithPath("meetings[].startTime").type(JsonFieldType.STRING).description("회의 시작 시간"),
                                fieldWithPath("meetings[].endTime").type(JsonFieldType.STRING).description("회의 종료 시간"),
                                fieldWithPath("meetings[].operationTeamId").type(JsonFieldType.NUMBER).description("회의 주최 팀 ID (현재 개발 중)"),
                                fieldWithPath("meetings[].operationTeamName").type(JsonFieldType.STRING).description("회의 주최 팀 명(현재 개발 중)"),
                                fieldWithPath("meetings[].meetingStatus").type(JsonFieldType.STRING).description("회의 상태 / EXPECT : 회의 예정, END : 회의 종료")
                        )
                ));
    }

    @DisplayName("오늘의 회의 리스트 조회 - 실패 / 워크스페이스, 워크스페이스 회원 ID를 파라미터로 주지 않을 경우")
    @Test
    void today_meetings_list_fail_required_workspace_workspace_user_id() throws Exception {

        // given
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("workspaceId", "");
        params.add("workspaceUserId", "");

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/meetings/today")
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .params(params))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(2)));
    }

    private List<TodayMeetingResponseDto> createTodayMeetingResponseDto() {
        LocalTime now = LocalTime.now();
        return List.of(
                TodayMeetingResponseDto.builder()
                    .meetingId(1L)
                    .meetingName("테스트 회의 1")
                    .meetingDate(LocalDate.now())
                    .startTime(LocalTime.of(now.plusHours(2).getHour(), 0, 0))
                    .endTime(LocalTime.of(now.plusHours(3).getHour(), 0, 0))
                    .operationTeamId(1L)
                    .operationTeamName("테스트 팀 1")
                    .meetingStatus("EXPECT")
                    .build(),
                TodayMeetingResponseDto.builder()
                        .meetingId(2L)
                        .meetingName("테스트 회의 2")
                        .meetingDate(LocalDate.now())
                        .startTime(LocalTime.of(now.minusHours(3).getHour(), 0, 0))
                        .endTime(LocalTime.of(now.minusHours(2).getHour(), 0, 0))
                        .operationTeamId(2L)
                        .operationTeamName("테스트 팀 2")
                        .meetingStatus("END")
                        .build()
        );
    }
}
