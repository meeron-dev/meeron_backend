package com.cmc.meeron.meeting.adapter.in;

import com.cmc.meeron.common.exception.meeting.AgendaNotFoundException;
import com.cmc.meeron.common.exception.meeting.MeetingErrorCode;
import com.cmc.meeron.meeting.application.port.in.response.AgendaCountResponseDto;
import com.cmc.meeron.meeting.application.port.in.response.AgendaCountResponseDtoBuilder;
import com.cmc.meeron.meeting.application.port.in.response.AgendaIssuesFilesResponseDto;
import com.cmc.meeron.meeting.application.port.in.response.AgendaIssuesFilesResponseDtoBuilder;
import com.cmc.meeron.support.restdocs.RestDocsTestSupport;
import com.cmc.meeron.support.security.WithMockJwt;
import com.google.common.net.HttpHeaders;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;

import static com.cmc.meeron.config.RestDocsConfig.field;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.any;
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
}
