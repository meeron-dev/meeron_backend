package com.cmc.meeron;

import com.cmc.meeron.support.SecuritySupport;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.hamcrest.Matchers.blankOrNullString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HealthRestController.class)
class HealthRestControllerTest extends SecuritySupport {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired WebApplicationContext webApplicationContext;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.webApplicationContext)
                .apply(springSecurity())
                .alwaysDo(print())
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }

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
