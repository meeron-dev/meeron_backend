package com.cmc.meeron.file.adapter.in;

import com.cmc.meeron.common.exception.CommonErrorCode;
import com.cmc.meeron.common.exception.meeting.AgendaNotFoundException;
import com.cmc.meeron.support.restdocs.RestDocsTestSupport;
import com.cmc.meeron.support.security.WithMockJwt;
import com.google.common.net.HttpHeaders;
import org.apache.http.entity.ContentType;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.cmc.meeron.config.RestDocsConfig.field;
import static com.cmc.meeron.file.FileFixture.FILE;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
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
                .andExpect(jsonPath("$.code", is(CommonErrorCode.APPLICATION_EXCEPTION.getCode())));
    }

    // TODO: 2022/03/09 kobeomseok95 확장자 명 검증하기
    @Disabled
    @DisplayName("아젠다 파일 생성 - 실패 / 파일에 확장자 명이 없을 경우")
    @Test
    void create_agenda_files_fail_not_found_extension() throws Exception {

        // given
        MockMultipartFile file = new MockMultipartFile("test", "test", ContentType.IMAGE_JPEG.getMimeType(), "test".getBytes());

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.multipart("/api/agendas/{agendaId}/files", "1")
                .file("files", file.getBytes())
                .header(HttpHeaders.AUTHORIZATION, "Bearer TestAccessToken")
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.code", is("MEERON-400")));
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
                .andExpect(jsonPath("$.code", is(CommonErrorCode.APPLICATION_EXCEPTION.getCode())));
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
}
