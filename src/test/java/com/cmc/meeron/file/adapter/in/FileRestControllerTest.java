package com.cmc.meeron.file.adapter.in;

import com.cmc.meeron.common.exception.file.FileErrorCode;
import com.cmc.meeron.common.exception.meeting.AgendaNotFoundException;
import com.cmc.meeron.common.exception.meeting.MeetingErrorCode;
import com.cmc.meeron.file.application.port.in.response.AgendaFileResponseDto;
import com.cmc.meeron.file.application.port.in.response.AgendaFileResponseDtoBuilder;
import com.cmc.meeron.support.restdocs.RestDocsTestSupport;
import com.cmc.meeron.support.security.WithMockJwt;
import com.google.common.net.HttpHeaders;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.cmc.meeron.config.RestDocsConfig.field;
import static com.cmc.meeron.file.FileFixture.FILE;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockJwt
class FileRestControllerTest extends RestDocsTestSupport {

    @DisplayName("아젠다 파일 생성 - 실패 / 파일이 존재하지 않을 경우")
    @Test
    void create_agenda_files_fail_not_empty_files() throws Exception {

        // given, when, then ,docs
        mockMvc.perform(RestDocumentationRequestBuilders.multipart("/api/agendas/{agendaId}/files", "1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.code", is(FileErrorCode.FILE_UPLOAD.getCode())));
    }

    @DisplayName("아젠다 파일 생성 - 실패 / 아젠다가 존재하지 않을 경우")
    @Test
    void create_agenda_files_fail_not_found_agenda() throws Exception {

        // given
        List<MultipartFile> files = createFiles();
        doThrow(new AgendaNotFoundException())
                .when(fileManager)
                .saveAgendaFiles(any(), any());

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.multipart("/api/agendas/{agendaId}/files", "1")
                .file("files", files.get(0).getBytes())
                .file("files", files.get(1).getBytes())
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.code", is(MeetingErrorCode.NOT_FOUND_AGENDA.getCode())));
    }

    @DisplayName("아젠다 파일 생성 - 성공")
    @Test
    void create_agenda_files_success() throws Exception {

        // given
        List<MultipartFile> files = createFiles();

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.multipart("/api/agendas/{agendaId}/files", "1")
                .file("files", files.get(0).getBytes())
                .file("files", files.get(1).getBytes())
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                .andDo(restDocumentationResultHandler.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access Token").attributes(field("constraints", "JWT Access Token With Bearer"))
                        ),
                        pathParameters(
                                parameterWithName("agendaId").description("파일을 업로드할 아젠다 ID")
                        ),
                        requestParts(
                                partWithName("files").description("업로드할 파일")
                        )
                ));
    }

    public List<MultipartFile> createFiles() {
        return List.of(
                FILE,
                FILE
        );
    }

    @DisplayName("아젠다의 파일들 조회 - 성공")
    @Test
    void get_agenda_files_success() throws Exception {

        // given
        List<AgendaFileResponseDto> responseDtos = AgendaFileResponseDtoBuilder.buildList();
        when(fileManager.getAgendaFiles(any()))
                .thenReturn(responseDtos);

        // when, then, docs
        AgendaFileResponseDto one = responseDtos.get(0);
        AgendaFileResponseDto two = responseDtos.get(1);
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/agendas/{agendaId}/files", "1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.files", hasSize(responseDtos.size())))
                .andExpect(jsonPath("$.files[0].fileId", is(one.getFileId().intValue())))
                .andExpect(jsonPath("$.files[0].fileName", is(one.getFileName())))
                .andExpect(jsonPath("$.files[0].fileUrl", is(one.getFileUrl())))
                .andExpect(jsonPath("$.files[1].fileId", is(two.getFileId().intValue())))
                .andExpect(jsonPath("$.files[1].fileName", is(two.getFileName())))
                .andExpect(jsonPath("$.files[1].fileUrl", is(two.getFileUrl())))
                .andDo(restDocumentationResultHandler.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access Token").attributes(field("constraints", "JWT Access Token With Bearer"))
                        ),
                        pathParameters(
                                parameterWithName("agendaId").description("아젠다 ID")
                        ),
                        responseFields(
                                fieldWithPath("files[].fileId").type(JsonFieldType.NUMBER).description("아젠다 파일 ID"),
                                fieldWithPath("files[].fileName").type(JsonFieldType.STRING).description("파일 원본 명"),
                                fieldWithPath("files[].fileUrl").type(JsonFieldType.STRING).description("파일 저장 URL")
                        )
                ));
    }
}
