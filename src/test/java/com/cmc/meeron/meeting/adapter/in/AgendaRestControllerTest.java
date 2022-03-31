package com.cmc.meeron.meeting.adapter.in;

import com.cmc.meeron.meeting.application.port.in.response.AgendaCountResponseDtoBuilder;
import com.cmc.meeron.meeting.application.port.in.response.AgendaCountResponseDto;
import com.cmc.meeron.support.restdocs.RestDocsTestSupport;
import com.cmc.meeron.support.security.WithMockJwt;
import com.google.common.net.HttpHeaders;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;

import static com.cmc.meeron.config.RestDocsConfig.field;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
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
                .andExpect(jsonPath("$.active", is(true)))
                .andExpect(jsonPath("$.checks", is(responseDto.getChecks())))
                .andExpect(jsonPath("$.files", is(responseDto.getFiles())))
                .andDo(restDocumentationResultHandler.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access Token").attributes(field("constraints", "JWT Access Token With Bearer"))
                        ),
                        pathParameters(
                                parameterWithName("meetingId").description("참가자를 찾을 회의 ID")
                        ),
                        responseFields(
                                fieldWithPath("active").type(JsonFieldType.BOOLEAN).description("화면 활성화 여부"),
                                fieldWithPath("checks").type(JsonFieldType.NUMBER).description("회의 확인 수"),
                                fieldWithPath("files").type(JsonFieldType.NUMBER).description("회의 아젠다의 파일 수")
                        )
                ));
    }
}