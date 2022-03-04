package com.cmc.meeron.meeting.presentation;

import com.cmc.meeron.meeting.application.dto.response.*;
import com.cmc.meeron.support.restdocs.RestDocsTestSupport;
import com.cmc.meeron.support.security.WithMockJwt;
import com.cmc.meeron.util.LocalDateTimeUtil;
import com.google.common.net.HttpHeaders;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Stream;

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
        when(meetingQueryUseCase.getTodayMeetings(any()))
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

    private List<Integer> getDays() {
        return List.of(10, 20, 15, 21);
    }

    @DisplayName("워크스페이스 캘린더에서 회의 날짜 조회 - 실패 / date(yyyy-MM)를 제대로 주지 않을 경우")
    @ParameterizedTest
    @MethodSource("failMeetingDaysParameters")
    void get_workspace_meeting_days_fail_require_yearMonth(MultiValueMap<String, String> params) throws Exception {

        // given, when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/meetings/days")
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .params(params)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.code", is("MEERON-400")))
                .andExpect(jsonPath("$.errors", hasSize(3)));
    }

    private static Stream<Arguments> failMeetingDaysParameters() {
        MultiValueMap<String, String> params1 = new LinkedMultiValueMap<>();
        params1.add("date", "2022-");
        MultiValueMap<String, String> params2 = new LinkedMultiValueMap<>();
        params2.add("date", "20");
        MultiValueMap<String, String> params3 = new LinkedMultiValueMap<>();
        params3.add("date", "");
        MultiValueMap<String, String> params4 = new LinkedMultiValueMap<>();
        params4.add("date", null);
        return Stream.of(
                Arguments.of(params1),
                Arguments.of(params2),
                Arguments.of(params3),
                Arguments.of(params4)
        );
    }

    @DisplayName("캘린더에서 이번 달 회의 날짜 조회 - 성공 / 워크스페이스 캘린더의 경우")
    @Test
    void get_meeting_days_success_workspace() throws Exception {

        // given
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("date", "2022-03");
        params.add("type", "workspace");
        params.add("id", "1");
        List<Integer> days = getDays();
        when(meetingQueryUseCase.getMeetingDays(any()))
                .thenReturn(days);

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/meetings/days")
                .params(params)
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.days", hasSize(4)))
                .andExpect(jsonPath("$.days[0]", is(days.get(0))))
                .andExpect(jsonPath("$.days[1]", is(days.get(1))))
                .andExpect(jsonPath("$.days[2]", is(days.get(2))))
                .andExpect(jsonPath("$.days[3]", is(days.get(3))))
                .andDo(restDocumentationResultHandler.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access Token").attributes(field("constraints", "JWT Access Token With Bearer"))
                        ),
                        requestParameters(
                                parameterWithName("type").description("워크스페이스의 경우 'workspace' 입력").attributes(field("constraints", "해당 파라미터는 workspace, workspace_user, team 중 하나")),
                                parameterWithName("id").description("찾을 워크스페이스 ID 입력"),
                                parameterWithName("date").description("찾을 년 월").attributes(field("constraints", "yyyy-MM 형식으로 입력"))
                        ),
                        responseFields(
                                fieldWithPath("days[]").description("검색하는 년 월에 해당하는 회의 날짜(day)들")
                        )
                ));
    }

    @DisplayName("캘린더에서 이번 달 회의 날짜 조회 - 성공 / 나의 미론, 내 캘린더의 경우")
    @Test
    void get_meeting_days_success_workspace_user() throws Exception {

        // given
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("date", "2022-03");
        params.add("type", "workspace_user");
        params.add("id", "1");
        params.add("id", "2");
        params.add("id", "3");
        List<Integer> days = getDays();
        when(meetingQueryUseCase.getMeetingDays(any()))
                .thenReturn(days);

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/meetings/days")
                .params(params)
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.days", hasSize(4)))
                .andExpect(jsonPath("$.days[0]", is(days.get(0))))
                .andExpect(jsonPath("$.days[1]", is(days.get(1))))
                .andExpect(jsonPath("$.days[2]", is(days.get(2))))
                .andExpect(jsonPath("$.days[3]", is(days.get(3))))
                .andDo(restDocumentationResultHandler.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access Token").attributes(field("constraints", "JWT Access Token With Bearer"))
                        ),
                        requestParameters(
                                parameterWithName("type").description("나의 캘린더의 경우 'workspace_user' 입력").attributes(field("constraints", "해당 파라미터는 workspace, workspace_user, team 중 하나")),
                                parameterWithName("id").description("접속한 유저의 모든 워크스페이스 유저 ID 입력").attributes(field("constraints", "workspace_user의 경우 여러 workspace_user_id가 존재, 리스트로 줄 수 있음")),
                                parameterWithName("date").description("찾을 년 월").attributes(field("constraints", "yyyy-MM 형식으로 입력"))
                        ),
                        responseFields(
                                fieldWithPath("days[]").description("검색하는 년 월에 해당하는 회의 날짜(day)들")
                        )
                ));
    }

    @DisplayName("캘린더에서 이번 달 회의 날짜 조회 - 성공 / 팀 캘린더의 경우")
    @Test
    void get_meeting_days_success_team() throws Exception {

        // given
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("date", "2022-03");
        params.add("type", "team");
        params.add("id", "1");
        List<Integer> days = getDays();
        when(meetingQueryUseCase.getMeetingDays(any()))
                .thenReturn(days);

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/meetings/days")
                .params(params)
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.days", hasSize(4)))
                .andExpect(jsonPath("$.days[0]", is(days.get(0))))
                .andExpect(jsonPath("$.days[1]", is(days.get(1))))
                .andExpect(jsonPath("$.days[2]", is(days.get(2))))
                .andExpect(jsonPath("$.days[3]", is(days.get(3))))
                .andDo(restDocumentationResultHandler.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access Token").attributes(field("constraints", "JWT Access Token With Bearer"))
                        ),
                        requestParameters(
                                parameterWithName("type").description("팀 캘린더의 경우 'team' 입력").attributes(field("constraints", "해당 파라미터는 workspace, workspace_user, team 중 하나")),
                                parameterWithName("id").description("팀 ID 입력"),
                                parameterWithName("date").description("찾을 년 월").attributes(field("constraints", "yyyy-MM 형식으로 입력"))
                        ),
                        responseFields(
                                fieldWithPath("days[]").description("검색하는 년 월에 해당하는 회의 날짜(day)들")
                        )
                ));
    }

    @DisplayName("워크스페이스 캘린더에서 선택한 날짜의 회의 조회 - 실패 / date(yyyy-MM-dd)를 제대로 주지 않을 경우")
    @ParameterizedTest
    @MethodSource("failDayMeetingsParameters")
    void get_workspace_day_meetings_fail_require_localDate(MultiValueMap<String, String> params) throws Exception {

        // given, when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/meetings/day")
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .params(params)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.code", is("MEERON-400")))
                .andExpect(jsonPath("$.errors", hasSize(3)));
    }

    private static Stream<Arguments> failDayMeetingsParameters() {
        MultiValueMap<String, String> params1 = new LinkedMultiValueMap<>();
        params1.add("date", "2022-");
        MultiValueMap<String, String> params2 = new LinkedMultiValueMap<>();
        params2.add("date", "20");
        MultiValueMap<String, String> params3 = new LinkedMultiValueMap<>();
        params3.add("date", "");
        MultiValueMap<String, String> params4 = new LinkedMultiValueMap<>();
        params4.add("date", null);
        MultiValueMap<String, String> params5 = new LinkedMultiValueMap<>();
        params5.add("date", "2022-02");
        MultiValueMap<String, String> params6 = new LinkedMultiValueMap<>();
        params6.add("date", "2022-02-1");
        return Stream.of(
                Arguments.of(params1),
                Arguments.of(params2),
                Arguments.of(params3),
                Arguments.of(params4),
                Arguments.of(params5),
                Arguments.of(params6)
        );
    }

    @DisplayName("캘린더에서 선택한 날짜의 회의 조회 - 성공 / 워크스페이스의 경우")
    @Test
    void get_day_meetings_success_workspace() throws Exception {

        // given
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        String now = LocalDate.now().toString();
        params.add("date", now);
        params.add("type", "workspace");
        params.add("id", "1");
        List<WorkspaceAndTeamDayMeetingResponseDto> responseDtos = getDayMeetingsWorkspaceAndTeam();
        when(meetingQueryUseCase.getWorkspaceAndTeamDayMeetings(any()))
                .thenReturn(responseDtos);

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/meetings/day")
                .params(params)
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meetings", hasSize(2)))
                .andExpect(jsonPath("$.meetings[0].meetingId", is(responseDtos.get(0).getMeetingId().intValue())))
                .andExpect(jsonPath("$.meetings[0].meetingName", is(responseDtos.get(0).getMeetingName())))
                .andExpect(jsonPath("$.meetings[0].startTime", is(responseDtos.get(0).getStartTime())))
                .andExpect(jsonPath("$.meetings[0].endTime", is(responseDtos.get(0).getEndTime())))
                .andExpect(jsonPath("$.meetings[0].workspaceId", nullValue()))
                .andExpect(jsonPath("$.meetings[0].workspaceName", nullValue()))
                .andExpect(jsonPath("$.meetings[1].meetingId", is(responseDtos.get(1).getMeetingId().intValue())))
                .andExpect(jsonPath("$.meetings[1].meetingName", is(responseDtos.get(1).getMeetingName())))
                .andExpect(jsonPath("$.meetings[1].startTime", is(responseDtos.get(1).getStartTime())))
                .andExpect(jsonPath("$.meetings[1].endTime", is(responseDtos.get(1).getEndTime())))
                .andExpect(jsonPath("$.meetings[1].workspaceId", nullValue()))
                .andExpect(jsonPath("$.meetings[1].workspaceName", nullValue()))
                .andDo(restDocumentationResultHandler.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access Token").attributes(field("constraints", "JWT Access Token With Bearer"))
                        ),
                        requestParameters(
                                parameterWithName("type").description("워크스페이스의 경우 'workspace' 입력").attributes(field("constraints", "해당 파라미터는 workspace, workspace_user, team 중 하나")),
                                parameterWithName("id").description("찾을 워크스페이스 ID 입력"),
                                parameterWithName("date").description("찾을 년 월 일").attributes(field("constraints", "yyyy-MM-dd 형식으로 입력"))
                        ),
                        responseFields(
                                fieldWithPath("meetings[].meetingId").type(JsonFieldType.NUMBER).description("회의 ID"),
                                fieldWithPath("meetings[].meetingName").type(JsonFieldType.STRING).description("회의 명"),
                                fieldWithPath("meetings[].startTime").type(JsonFieldType.STRING).description("회의 시작 시간"),
                                fieldWithPath("meetings[].endTime").type(JsonFieldType.STRING).description("회의 종료 시간"),
                                fieldWithPath("meetings[].workspaceId").type(JsonFieldType.NULL).description("워크스페이스 ID / 워크스페이스, 팀 조회 시 null"),
                                fieldWithPath("meetings[].workspaceName").type(JsonFieldType.NULL).description("워크스페이스 명 / 워크스페이스, 팀 조회 시 null")
                        )
                ));
    }

    private List<WorkspaceAndTeamDayMeetingResponseDto> getDayMeetingsWorkspaceAndTeam() {
        LocalTime now = LocalTime.now();
        return List.of(
                WorkspaceAndTeamDayMeetingResponseDto.builder()
                        .meetingId(1L)
                        .meetingName("첫번째회의")
                        .startTime(LocalDateTimeUtil.convertTime(now.plusHours(1)))
                        .endTime(LocalDateTimeUtil.convertTime((now.plusHours(3))))
                        .build(),
                WorkspaceAndTeamDayMeetingResponseDto.builder()
                        .meetingId(2L)
                        .meetingName("두번째회의")
                        .startTime(LocalDateTimeUtil.convertTime(now.plusHours(4)))
                        .endTime(LocalDateTimeUtil.convertTime(now.plusHours(6)))
                        .build()
        );
    }

    @DisplayName("캘린더에서 선택한 날짜의 회의 조회 - 성공 / 나의 미론, 내 캘린더의 경우")
    @Test
    void get_day_meetings_success_workspace_user() throws Exception {

        // given
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        String now = LocalDate.now().toString();
        params.add("date", now);
        params.add("type", "workspace_user");
        params.add("id", "1");
        params.add("id", "2");
        params.add("id", "3");
        List<WorkspaceUserDayMeetingResponseDto> responseDtos = getDayMeetingsWorkspaceUser();
        when(meetingQueryUseCase.getWorkspaceUserDayMeetings(any()))
                .thenReturn(responseDtos);

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/meetings/day")
                .params(params)
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meetings", hasSize(2)))
                .andExpect(jsonPath("$.meetings[0].meetingId", is(responseDtos.get(0).getMeetingId().intValue())))
                .andExpect(jsonPath("$.meetings[0].meetingName", is(responseDtos.get(0).getMeetingName())))
                .andExpect(jsonPath("$.meetings[0].startTime", is(responseDtos.get(0).getStartTime())))
                .andExpect(jsonPath("$.meetings[0].endTime", is(responseDtos.get(0).getEndTime())))
                .andExpect(jsonPath("$.meetings[0].workspaceId", is(responseDtos.get(0).getWorkspaceId().intValue())))
                .andExpect(jsonPath("$.meetings[0].workspaceName", is(responseDtos.get(0).getWorkspaceName())))
                .andExpect(jsonPath("$.meetings[1].meetingId", is(responseDtos.get(1).getMeetingId().intValue())))
                .andExpect(jsonPath("$.meetings[1].meetingName", is(responseDtos.get(1).getMeetingName())))
                .andExpect(jsonPath("$.meetings[1].startTime", is(responseDtos.get(1).getStartTime())))
                .andExpect(jsonPath("$.meetings[1].endTime", is(responseDtos.get(1).getEndTime())))
                .andExpect(jsonPath("$.meetings[1].workspaceId", is(responseDtos.get(1).getWorkspaceId().intValue())))
                .andExpect(jsonPath("$.meetings[1].workspaceName", is(responseDtos.get(1).getWorkspaceName())))
                .andDo(restDocumentationResultHandler.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access Token").attributes(field("constraints", "JWT Access Token With Bearer"))
                        ),
                        requestParameters(
                                parameterWithName("type").description("나의 캘린더의 경우 'workspace_user' 입력").attributes(field("constraints", "해당 파라미터는 workspace, workspace_user, team 중 하나")),
                                parameterWithName("id").description("접속한 유저의 모든 워크스페이스 유저 ID 입력").attributes(field("constraints", "workspace_user의 경우 여러 workspace_user_id가 존재, 리스트로 줄 수 있음")),
                                parameterWithName("date").description("찾을 년 월 일").attributes(field("constraints", "yyyy-MM-dd 형식으로 입력"))
                        ),
                        responseFields(
                                fieldWithPath("meetings[].meetingId").type(JsonFieldType.NUMBER).description("회의 ID"),
                                fieldWithPath("meetings[].meetingName").type(JsonFieldType.STRING).description("회의 명"),
                                fieldWithPath("meetings[].startTime").type(JsonFieldType.STRING).description("회의 시작 시간"),
                                fieldWithPath("meetings[].endTime").type(JsonFieldType.STRING).description("회의 종료 시간"),
                                fieldWithPath("meetings[].workspaceId").type(JsonFieldType.NUMBER).description("워크스페이스 ID / 워크스페이스, 팀 조회 시 null"),
                                fieldWithPath("meetings[].workspaceName").type(JsonFieldType.STRING).description("워크스페이스 명 / 워크스페이스, 팀 조회 시 null")
                        )
                ));
    }

    private List<WorkspaceUserDayMeetingResponseDto> getDayMeetingsWorkspaceUser() {
        LocalTime now = LocalTime.now();
        return List.of(
                WorkspaceUserDayMeetingResponseDto.builder()
                        .meetingId(1L)
                        .meetingName("첫번째회의")
                        .startTime(LocalDateTimeUtil.convertTime(now.plusHours(1)))
                        .endTime(LocalDateTimeUtil.convertTime((now.plusHours(3))))
                        .workspaceId(1L)
                        .workspaceName("첫번째 워크스페이스")
                        .build(),
                WorkspaceUserDayMeetingResponseDto.builder()
                        .meetingId(2L)
                        .meetingName("두번째회의")
                        .startTime(LocalDateTimeUtil.convertTime(now.plusHours(4)))
                        .endTime(LocalDateTimeUtil.convertTime((now.plusHours(6))))
                        .workspaceId(2L)
                        .workspaceName("두번째 워크스페이스")
                        .build()
        );
    }

    @DisplayName("캘린더에서 선택한 날짜의 회의 조회 - 성공 / 팀의 경우")
    @Test
    void get_day_meetings_success_team() throws Exception {

        // given
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        String now = LocalDate.now().toString();
        params.add("date", now);
        params.add("type", "team");
        params.add("id", "1");
        List<WorkspaceAndTeamDayMeetingResponseDto> responseDtos = getDayMeetingsWorkspaceAndTeam();
        when(meetingQueryUseCase.getWorkspaceAndTeamDayMeetings(any()))
                .thenReturn(responseDtos);

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/meetings/day")
                .params(params)
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meetings", hasSize(2)))
                .andExpect(jsonPath("$.meetings[0].meetingId", is(responseDtos.get(0).getMeetingId().intValue())))
                .andExpect(jsonPath("$.meetings[0].meetingName", is(responseDtos.get(0).getMeetingName())))
                .andExpect(jsonPath("$.meetings[0].startTime", is(responseDtos.get(0).getStartTime())))
                .andExpect(jsonPath("$.meetings[0].endTime", is(responseDtos.get(0).getEndTime())))
                .andExpect(jsonPath("$.meetings[1].meetingId", is(responseDtos.get(1).getMeetingId().intValue())))
                .andExpect(jsonPath("$.meetings[1].meetingName", is(responseDtos.get(1).getMeetingName())))
                .andExpect(jsonPath("$.meetings[1].startTime", is(responseDtos.get(1).getStartTime())))
                .andExpect(jsonPath("$.meetings[1].endTime", is(responseDtos.get(1).getEndTime())))
                .andDo(restDocumentationResultHandler.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access Token").attributes(field("constraints", "JWT Access Token With Bearer"))
                        ),
                        requestParameters(
                                parameterWithName("type").description("팀 캘린더의 경우 'team' 입력").attributes(field("constraints", "해당 파라미터는 workspace, workspace_user, team 중 하나")),
                                parameterWithName("id").description("팀 ID 입력"),
                                parameterWithName("date").description("찾을 년 월").attributes(field("constraints", "yyyy-MM-dd 형식으로 입력"))
                        ),
                        responseFields(
                                fieldWithPath("meetings[].meetingId").type(JsonFieldType.NUMBER).description("회의 ID"),
                                fieldWithPath("meetings[].meetingName").type(JsonFieldType.STRING).description("회의 명"),
                                fieldWithPath("meetings[].startTime").type(JsonFieldType.STRING).description("회의 시작 시간"),
                                fieldWithPath("meetings[].endTime").type(JsonFieldType.STRING).description("회의 종료 시간"),
                                fieldWithPath("meetings[].workspaceId").type(JsonFieldType.NULL).description("워크스페이스 ID / 워크스페이스, 팀 조회 시 null"),
                                fieldWithPath("meetings[].workspaceName").type(JsonFieldType.NULL).description("워크스페이스 명 / 워크스페이스, 팀 조회 시 null")
                        )
                ));
    }

    @DisplayName("캘린더에서 년도별 회의 갯수 조회 - 성공 / 워크스페이스 캘린더의 경우")
    @Test
    void get_year_meetings_count_success_workspace() throws Exception {

        // given
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("type", "workspace");
        params.add("id", "1");
        List<YearMeetingsCountResponseDto> responseDtos = createYearMeetingsCountResposeDtos();
        when(meetingQueryUseCase.getYearMeetingsCount(any()))
                .thenReturn(responseDtos);

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/meetings/years")
                .params(params)
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.yearCounts", hasSize(2)))
                .andExpect(jsonPath("$.yearCounts[0].year", is(responseDtos.get(0).getYear())))
                .andExpect(jsonPath("$.yearCounts[0].count", is(responseDtos.get(0).getCount().intValue())))
                .andExpect(jsonPath("$.yearCounts[1].year", is(responseDtos.get(1).getYear())))
                .andExpect(jsonPath("$.yearCounts[1].count", is(responseDtos.get(1).getCount().intValue())))
                .andDo(restDocumentationResultHandler.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access Token").attributes(field("constraints", "JWT Access Token With Bearer"))
                        ),
                        requestParameters(
                                parameterWithName("type").description("워크스페이스의 경우 'workspace' 입력").attributes(field("constraints", "해당 파라미터는 workspace, workspace_user, team 중 하나")),
                                parameterWithName("id").description("찾을 워크스페이스 ID 입력")
                        ),
                        responseFields(
                                fieldWithPath("yearCounts[].year").type(JsonFieldType.NUMBER).description("회의가 존재하는 년도"),
                                fieldWithPath("yearCounts[].count").type(JsonFieldType.NUMBER).description("해당 년도의 회의 갯수")
                        )
                ));
    }

    private List<YearMeetingsCountResponseDto> createYearMeetingsCountResposeDtos() {
        return List.of(
                YearMeetingsCountResponseDto.builder().year(2022).count(12L).build(),
                YearMeetingsCountResponseDto.builder().year(2021).count(13L).build()
        );
    }

    @DisplayName("캘린더에서 년도별 회의 갯수 조회 - 성공 / 나의 미론, 내 캘린더의 경우")
    @Test
    void get_year_meetings_count_success_workspace_user() throws Exception {

        // given
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("type", "workspace_user");
        params.add("id", "1");
        params.add("id", "2");
        params.add("id", "3");
        List<YearMeetingsCountResponseDto> responseDtos = createYearMeetingsCountResposeDtos();
        when(meetingQueryUseCase.getYearMeetingsCount(any()))
                .thenReturn(responseDtos);

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/meetings/years")
                .params(params)
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.yearCounts", hasSize(2)))
                .andExpect(jsonPath("$.yearCounts[0].year", is(responseDtos.get(0).getYear())))
                .andExpect(jsonPath("$.yearCounts[0].count", is(responseDtos.get(0).getCount().intValue())))
                .andExpect(jsonPath("$.yearCounts[1].year", is(responseDtos.get(1).getYear())))
                .andExpect(jsonPath("$.yearCounts[1].count", is(responseDtos.get(1).getCount().intValue())))
                .andDo(restDocumentationResultHandler.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access Token").attributes(field("constraints", "JWT Access Token With Bearer"))
                        ),
                        requestParameters(
                                parameterWithName("type").description("나의 캘린더의 경우 'workspace_user' 입력").attributes(field("constraints", "해당 파라미터는 workspace, workspace_user, team 중 하나")),
                                parameterWithName("id").description("접속한 유저의 모든 워크스페이스 유저 ID 입력").attributes(field("constraints", "workspace_user의 경우 여러 workspace_user_id가 존재, 리스트로 줄 수 있음"))
                        ),
                        responseFields(
                                fieldWithPath("yearCounts[].year").type(JsonFieldType.NUMBER).description("회의가 존재하는 년도"),
                                fieldWithPath("yearCounts[].count").type(JsonFieldType.NUMBER).description("해당 년도의 회의 갯수")
                        )
                ));
    }

    @DisplayName("캘린더에서 년도별 회의 갯수 조회 - 성공 / 팀 캘린더의 경우")
    @Test
    void get_year_meetings_count_success_team() throws Exception {

        // given
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("type", "team");
        params.add("id", "1");
        List<YearMeetingsCountResponseDto> responseDtos = createYearMeetingsCountResposeDtos();
        when(meetingQueryUseCase.getYearMeetingsCount(any()))
                .thenReturn(responseDtos);

        // when, then, docs

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/meetings/years")
                .params(params)
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.yearCounts", hasSize(2)))
                .andExpect(jsonPath("$.yearCounts[0].year", is(responseDtos.get(0).getYear())))
                .andExpect(jsonPath("$.yearCounts[0].count", is(responseDtos.get(0).getCount().intValue())))
                .andExpect(jsonPath("$.yearCounts[1].year", is(responseDtos.get(1).getYear())))
                .andExpect(jsonPath("$.yearCounts[1].count", is(responseDtos.get(1).getCount().intValue())))
                .andDo(restDocumentationResultHandler.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access Token").attributes(field("constraints", "JWT Access Token With Bearer"))
                        ),
                        requestParameters(
                                parameterWithName("type").description("팀 캘린더의 경우 'team' 입력").attributes(field("constraints", "해당 파라미터는 workspace, workspace_user, team 중 하나")),
                                parameterWithName("id").description("팀 ID 입력")
                        ),
                        responseFields(
                                fieldWithPath("yearCounts[].year").type(JsonFieldType.NUMBER).description("회의가 존재하는 년도"),
                                fieldWithPath("yearCounts[].count").type(JsonFieldType.NUMBER).description("해당 년도의 회의 갯수")
                        )
                ));
    }

    @DisplayName("캘린더에서 년도별 회의 갯수 조회 - 성공 / 워크스페이스 캘린더의 경우")
    @Test
    void get_month_meetings_count_success_workspace() throws Exception {

        // given
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("type", "workspace");
        params.add("id", "1");
        params.add("year", "2022");
        List<MonthMeetingsCountResponseDto> responseDtos = createMonthMeetingsCountResposeDtos();
        when(meetingQueryUseCase.getMonthMeetingsCount(any()))
                .thenReturn(responseDtos);

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/meetings/months")
                .params(params)
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.monthCounts", hasSize(12)))
                .andDo(restDocumentationResultHandler.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access Token").attributes(field("constraints", "JWT Access Token With Bearer"))
                        ),
                        requestParameters(
                                parameterWithName("type").description("워크스페이스의 경우 'workspace' 입력").attributes(field("constraints", "해당 파라미터는 workspace, workspace_user, team 중 하나")),
                                parameterWithName("id").description("찾을 워크스페이스 ID 입력"),
                                parameterWithName("year").description("찾을 년도")
                        ),
                        responseFields(
                                fieldWithPath("monthCounts[].month").type(JsonFieldType.NUMBER).description("선택한 년도의 월"),
                                fieldWithPath("monthCounts[].count").type(JsonFieldType.NUMBER).description("해당 월의 회의 갯수")
                        )
                ));
    }

    private List<MonthMeetingsCountResponseDto> createMonthMeetingsCountResposeDtos() {
        return List.of(
                MonthMeetingsCountResponseDto.builder().month(1).count(17L).build(),
                MonthMeetingsCountResponseDto.builder().month(2).count(12L).build(),
                MonthMeetingsCountResponseDto.builder().month(3).count(16L).build(),
                MonthMeetingsCountResponseDto.builder().month(4).count(29L).build(),
                MonthMeetingsCountResponseDto.builder().month(5).count(3L).build(),
                MonthMeetingsCountResponseDto.builder().month(6).count(0L).build(),
                MonthMeetingsCountResponseDto.builder().month(7).count(0L).build(),
                MonthMeetingsCountResponseDto.builder().month(8).count(0L).build(),
                MonthMeetingsCountResponseDto.builder().month(9).count(0L).build(),
                MonthMeetingsCountResponseDto.builder().month(10).count(1L).build(),
                MonthMeetingsCountResponseDto.builder().month(11).count(12L).build(),
                MonthMeetingsCountResponseDto.builder().month(12).count(25L).build()
        );
    }

    @DisplayName("캘린더에서 년도별 회의 갯수 조회 - 성공 / 나의 미론, 내 캘린더의 경우")
    @Test
    void get_month_meetings_count_success_workspace_user() throws Exception {

        // given
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("type", "workspace_user");
        params.add("id", "1");
        params.add("id", "2");
        params.add("id", "3");
        params.add("year", "2022");
        List<MonthMeetingsCountResponseDto> responseDtos = createMonthMeetingsCountResposeDtos();
        when(meetingQueryUseCase.getMonthMeetingsCount(any()))
                .thenReturn(responseDtos);

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/meetings/months")
                .params(params)
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.monthCounts", hasSize(12)))
                .andDo(restDocumentationResultHandler.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access Token").attributes(field("constraints", "JWT Access Token With Bearer"))
                        ),
                        requestParameters(
                                parameterWithName("type").description("나의 캘린더의 경우 'workspace_user' 입력").attributes(field("constraints", "해당 파라미터는 workspace, workspace_user, team 중 하나")),
                                parameterWithName("id").description("접속한 유저의 모든 워크스페이스 유저 ID 입력").attributes(field("constraints", "workspace_user의 경우 여러 workspace_user_id가 존재, 리스트로 줄 수 있음")),
                                parameterWithName("year").description("찾을 년도")
                        ),
                        responseFields(
                                fieldWithPath("monthCounts[].month").type(JsonFieldType.NUMBER).description("선택한 년도의 월"),
                                fieldWithPath("monthCounts[].count").type(JsonFieldType.NUMBER).description("해당 월의 회의 갯수")
                        )
                ));
    }

    @DisplayName("캘린더에서 년도별 회의 갯수 조회 - 성공 / 팀 캘린더의 경우")
    @Test
    void get_month_meetings_count_success_team() throws Exception {

        // given
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("type", "team");
        params.add("id", "1");
        params.add("year", "2022");
        List<MonthMeetingsCountResponseDto> responseDtos = createMonthMeetingsCountResposeDtos();
        when(meetingQueryUseCase.getMonthMeetingsCount(any()))
                .thenReturn(responseDtos);

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/meetings/months")
                .params(params)
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.monthCounts", hasSize(12)))
                .andDo(restDocumentationResultHandler.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access Token").attributes(field("constraints", "JWT Access Token With Bearer"))
                        ),
                        requestParameters(
                                parameterWithName("type").description("팀 캘린더의 경우 'team' 입력").attributes(field("constraints", "해당 파라미터는 workspace, workspace_user, team 중 하나")),
                                parameterWithName("id").description("팀 ID 입력"),
                                parameterWithName("year").description("찾을 년도")
                        ),
                        responseFields(
                                fieldWithPath("monthCounts[].month").type(JsonFieldType.NUMBER).description("회의가 존재하는 년도"),
                                fieldWithPath("monthCounts[].count").type(JsonFieldType.NUMBER).description("해당 년도의 회의 갯수")
                        )
                ));
    }
}
