package com.cmc.meeron.topic.issue.adapter.in;

import com.cmc.meeron.support.restdocs.RestDocsTestSupport;
import com.cmc.meeron.support.security.WithMockJwt;
import com.cmc.meeron.topic.issue.application.port.in.response.IssueResponseDto;
import com.cmc.meeron.topic.issue.application.port.in.response.IssueResponseDtoBuilder;
import com.google.common.net.HttpHeaders;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.List;

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
class IssueRestControllerTest extends RestDocsTestSupport {

    @DisplayName("아젠다의 이슈 조회 - 성공")
    @Test
    void get_agenda_issues_success() throws Exception {

        // given
        List<IssueResponseDto> responseDtos = IssueResponseDtoBuilder.buildList();
        when(issueQueryUseCase.getAgendaIssues(any()))
                .thenReturn(responseDtos);

        // when, then, docs
        IssueResponseDto one = responseDtos.get(0);
        IssueResponseDto two = responseDtos.get(1);
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/agendas/{agendaId}/issues",
                "1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.issues", hasSize(responseDtos.size())))
                .andExpect(jsonPath("$.issues[0].issueId", is(one.getIssueId().intValue())))
                .andExpect(jsonPath("$.issues[0].content", is(one.getContent())))
                .andExpect(jsonPath("$.issues[0].issueResult", is(one.getIssueResult())))
                .andExpect(jsonPath("$.issues[1].issueId", is(two.getIssueId().intValue())))
                .andExpect(jsonPath("$.issues[1].content", is(two.getContent())))
                .andExpect(jsonPath("$.issues[1].issueResult", is(two.getIssueResult())))
                .andDo(restDocumentationResultHandler.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access Token")
                        ),
                        pathParameters(
                                parameterWithName("agendaId").description("아젠다 ID")
                        ),
                        responseFields(
                                fieldWithPath("issues[].issueId").type(JsonFieldType.NUMBER).description("이슈 ID"),
                                fieldWithPath("issues[].content").type(JsonFieldType.STRING).description("이슈 내용"),
                                fieldWithPath("issues[].issueResult").type(JsonFieldType.STRING).description("이슈 결과")
                        )
                ));
    }
}
