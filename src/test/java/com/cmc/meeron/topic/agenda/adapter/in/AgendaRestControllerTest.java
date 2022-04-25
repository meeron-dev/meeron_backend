package com.cmc.meeron.topic.agenda.adapter.in;

import com.cmc.meeron.common.exception.ClientErrorCode;
import com.cmc.meeron.common.exception.meeting.MeetingErrorCode;
import com.cmc.meeron.common.exception.meeting.MeetingNotFoundException;
import com.cmc.meeron.common.exception.topic.agenda.AgendaErrorCode;
import com.cmc.meeron.common.exception.meeting.AgendaNotFoundException;
import com.cmc.meeron.support.restdocs.RestDocsTestSupport;
import com.cmc.meeron.support.security.WithMockJwt;
import com.cmc.meeron.topic.agenda.adapter.in.request.CreateAgendaRequest;
import com.cmc.meeron.topic.agenda.application.port.in.response.*;
import com.google.common.net.HttpHeaders;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.List;

import static com.cmc.meeron.config.RestDocsConfig.field;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockJwt
class AgendaRestControllerTest extends RestDocsTestSupport {

    @DisplayName("회의 아젠다 카운트 조회 - 성공")
    @Test
    void get_agenda_counts_success() throws Exception {

        // given
        AgendaCountResponseDto responseDto = AgendaCountResponseDtoBuilder.build();
        when(agendaQueryUseCase.getAgendaCountsByMeetingId(any()))
                .thenReturn(responseDto);

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/meetings/{meetingId}/agendas/count", "1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.agendas", is((int) responseDto.getAgendas())))
                .andExpect(jsonPath("$.checks", is((int) responseDto.getChecks())))
                .andExpect(jsonPath("$.files", is((int) responseDto.getFiles())))
                .andExpect(handler().handlerType(AgendaRestController.class))
                .andDo(restDocumentationResultHandler.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access Token").attributes(field("constraints", "JWT Access Token With Bearer"))
                        ),
                        pathParameters(
                                parameterWithName("meetingId").description("참가자를 찾을 회의 ID")
                        ),
                        responseFields(
                                fieldWithPath("agendas").type(JsonFieldType.NUMBER).description("회의 아젠다 수, 0일 경우 버튼 비활성화"),
                                fieldWithPath("checks").type(JsonFieldType.NUMBER).description("회의 확인 수"),
                                fieldWithPath("files").type(JsonFieldType.NUMBER).description("회의 아젠다의 파일 수")
                        )
                ));
    }

    @Deprecated
    @DisplayName("회의의 아젠다 조회 - 실패 / 아젠다가 없을 경우")
    @Test
    void get_agenda_file_issues_fail_not_found_agenda() throws Exception {

        // given
        when(agendaQueryUseCase.getAgendaIssuesFiles(any()))
                .thenThrow(new AgendaNotFoundException());

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/meetings/{meetingId}/agendas/{agendaOrder}",
                "1", "1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(MeetingErrorCode.NOT_FOUND_AGENDA.getCode())))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())));
    }

    @Deprecated
    @DisplayName("회의의 아젠다 조회 - 성공")
    @Test
    void get_agenda_file_issues_success() throws Exception {

        // given
        AgendaIssuesFilesResponseDto responseDto = AgendaIssuesFilesResponseDtoBuilder.build();
        when(agendaQueryUseCase.getAgendaIssuesFiles(any()))
                .thenReturn(responseDto);

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/meetings/{meetingId}/agendas/{agendaOrder}",
                "1", "1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.agendaId", is(responseDto.getAgendaId().intValue())))
                .andExpect(jsonPath("$.agendaName", is(responseDto.getAgendaName())))
                .andExpect(jsonPath("$.issues[0].issueId", is(responseDto.getIssues().get(0).getIssueId().intValue())))
                .andExpect(jsonPath("$.issues[0].content", is(responseDto.getIssues().get(0).getContent())))
                .andExpect(jsonPath("$.issues[1].issueId", is(responseDto.getIssues().get(1).getIssueId().intValue())))
                .andExpect(jsonPath("$.issues[1].content", is(responseDto.getIssues().get(1).getContent())))
                .andExpect(jsonPath("$.files[0].fileId", is(responseDto.getFiles().get(0).getFileId().intValue())))
                .andExpect(jsonPath("$.files[0].fileName", is(responseDto.getFiles().get(0).getFileName())))
                .andExpect(jsonPath("$.files[0].fileUrl", is(responseDto.getFiles().get(0).getFileUrl())))
                .andExpect(jsonPath("$.files[1].fileId", is(responseDto.getFiles().get(1).getFileId().intValue())))
                .andExpect(jsonPath("$.files[1].fileName", is(responseDto.getFiles().get(1).getFileName())))
                .andExpect(jsonPath("$.files[1].fileUrl", is(responseDto.getFiles().get(1).getFileUrl())))
                .andExpect(handler().handlerType(AgendaRestController.class))
                .andDo(restDocumentationResultHandler.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access Token").attributes(field("constraints", "JWT Access Token With Bearer"))
                        ),
                        pathParameters(
                                parameterWithName("meetingId").description("아젠다를 찾을 회의 ID"),
                                parameterWithName("agendaOrder").description("아젠다 번호 (ID 아닙니다.)")
                        ),
                        responseFields(
                                fieldWithPath("agendaId").type(JsonFieldType.NUMBER).description("아젠다 ID"),
                                fieldWithPath("agendaName").type(JsonFieldType.STRING).description("아젠다 제목"),
                                fieldWithPath("issues[].issueId").type(JsonFieldType.NUMBER).description("이슈 ID"),
                                fieldWithPath("issues[].content").type(JsonFieldType.STRING).description("이슈 내용"),
                                fieldWithPath("files[].fileId").type(JsonFieldType.NUMBER).description("아젠다 파일 ID"),
                                fieldWithPath("files[].fileName").type(JsonFieldType.STRING).description("아젠다 파일 원본명"),
                                fieldWithPath("files[].fileUrl").type(JsonFieldType.STRING).description("아젠다 저장 파일 URL")
                        )
                ));
    }

    @DisplayName("회의 아젠다 조회 - 성공")
    @Test
    void get_meeting_agendas_success() throws Exception {

        // given
        List<AgendaResponseDto> responseDtos = AgendaResponseDtoBuilder.buildList();
        when(agendaQueryUseCase.getMeetingAgendas(any()))
                .thenReturn(responseDtos);

        // when, when, docs
        AgendaResponseDto one = responseDtos.get(0);
        AgendaResponseDto two = responseDtos.get(1);
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/meetings/{meetingId}/agendas",
                "1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.agendas", hasSize(2)))
                .andExpect(jsonPath("$.agendas[0].agendaId", is(one.getAgendaId().intValue())))
                .andExpect(jsonPath("$.agendas[0].agendaName", is(one.getAgendaName())))
                .andExpect(jsonPath("$.agendas[0].agendaOrder", is(one.getAgendaOrder())))
                .andExpect(jsonPath("$.agendas[0].agendaResult", is(one.getAgendaResult())))
                .andExpect(jsonPath("$.agendas[1].agendaId", is(two.getAgendaId().intValue())))
                .andExpect(jsonPath("$.agendas[1].agendaName", is(two.getAgendaName())))
                .andExpect(jsonPath("$.agendas[1].agendaOrder", is(two.getAgendaOrder())))
                .andExpect(jsonPath("$.agendas[1].agendaResult", is(two.getAgendaResult())))
                .andExpect(handler().handlerType(AgendaRestController.class))
                .andDo(restDocumentationResultHandler.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access Token").attributes(field("constraints", "JWT Access Token With Bearer"))
                        ),
                        pathParameters(
                                parameterWithName("meetingId").description("아젠다를 찾을 회의 ID")
                        ),
                        responseFields(
                                fieldWithPath("agendas[].agendaId").type(JsonFieldType.NUMBER).description("아젠다 ID"),
                                fieldWithPath("agendas[].agendaName").type(JsonFieldType.STRING).description("아젠다 제목"),
                                fieldWithPath("agendas[].agendaOrder").type(JsonFieldType.NUMBER).description("아젠다 순서(정렬되어 있음)"),
                                fieldWithPath("agendas[].agendaResult").type(JsonFieldType.STRING).description("아젠다 결과")
                        )
                ));
    }

    @DisplayName("아젠다 상세 조회 - 실패 / 존재하지 않을 경우")
    @Test
    void get_agenda_fail_not_found() throws Exception {

        // given
        when(agendaQueryUseCase.getAgenda(any()))
                .thenThrow(new com.cmc.meeron.common.exception.topic.agenda.AgendaNotFoundException());

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/agendas/{agendaId}",
                "1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(AgendaErrorCode.NOT_FOUND.getCode())))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())));
    }

    @DisplayName("아젠다 상세 조회 - 성공")
    @Test
    void get_agenda_success() throws Exception {

        // given
        AgendaResponseDto responseDto = AgendaResponseDtoBuilder.build();
        when(agendaQueryUseCase.getAgenda(any()))
                .thenReturn(responseDto);

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/agendas/{agendaId}",
                "1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.agendaId", is(responseDto.getAgendaId().intValue())))
                .andExpect(jsonPath("$.agendaName", is(responseDto.getAgendaName())))
                .andExpect(jsonPath("$.agendaOrder", is(responseDto.getAgendaOrder())))
                .andExpect(jsonPath("$.agendaResult", is(responseDto.getAgendaResult())))
                .andExpect(handler().handlerType(AgendaRestController.class))
                .andDo(restDocumentationResultHandler.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access Token").attributes(field("constraints", "JWT Access Token With Bearer"))
                        ),
                        pathParameters(
                                parameterWithName("agendaId").description("아젠다 ID")
                        ),
                        responseFields(
                                fieldWithPath("agendaId").type(JsonFieldType.NUMBER).description("아젠다 ID"),
                                fieldWithPath("agendaName").type(JsonFieldType.STRING).description("아젠다 제목"),
                                fieldWithPath("agendaOrder").type(JsonFieldType.NUMBER).description("아젠다 순서(정렬되어 있음)"),
                                fieldWithPath("agendaResult").type(JsonFieldType.STRING).description("아젠다 결과")
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
        when(agendaCommandUseCase.createAgendas(any()))
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
        when(agendaCommandUseCase.createAgendas(any()))
                .thenReturn(createdAgendaIds);

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/meetings/{meetingId}/agendas", "1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.createdAgendaIds[0]", is(createdAgendaIds.get(0).intValue())))
                .andExpect(handler().handlerType(AgendaRestController.class))
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
}
