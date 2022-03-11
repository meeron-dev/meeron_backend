package com.cmc.meeron.file.integration;

import com.cmc.meeron.file.application.port.out.AgendaFileCommandPort;
import com.cmc.meeron.support.IntegrationTest;
import com.cmc.meeron.support.security.WithMockJwt;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.cmc.meeron.file.FileFixture.FILE;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockJwt
public class FileIntegrationTest extends IntegrationTest {

    @Autowired
    AgendaFileCommandPort agendaFileCommandPort;

    @DisplayName("아젠다 파일 생성 - 성공")
    @Test
    void create_agenda_file_success() throws Exception {

        // given, when, then
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/agendas/{agendaId}/files", 1)
                .file(FILE)
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated());
    }
}
