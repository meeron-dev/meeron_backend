package com.cmc.meeron;

import com.cmc.meeron.support.restdocs.RestDocsTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.blankOrNullString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class HealthRestControllerTest extends RestDocsTestSupport {

    @DisplayName("헬스 체크 / 성공")
    @Test
    void health_check_success() throws Exception {

        // given, when, then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("OK"));
    }

    @DisplayName("인증 체크 / 권한이 없을 경우")
    @Test
    void authentication_check_fail() throws Exception {

        // given
        setUpUnAuthenticated();

        // given, when, then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/health/authenticated")
                .header(HttpHeaders.AUTHORIZATION, "Bearer ex"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.time", not(blankOrNullString())))
                .andExpect(jsonPath("$.status", is(HttpStatus.UNAUTHORIZED.value())))
                .andExpect(jsonPath("$.message", not(blankOrNullString())))
                .andExpect(jsonPath("$.code", is("MEERON-401")));
    }

    @DisplayName("인증 체크 / 성공")
    @Test
    void authentication_check_success() throws Exception {

        // given
        setUpAuthenticated();

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/health/authenticated")
                .header(HttpHeaders.AUTHORIZATION, "Bearer testJWT"))
                .andExpect(content().string("AUTHENTICATED"));
    }
}
