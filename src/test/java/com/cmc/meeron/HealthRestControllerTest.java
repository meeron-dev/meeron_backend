package com.cmc.meeron;

import com.cmc.meeron.auth.handler.CustomUserDetailsService;
import com.cmc.meeron.auth.handler.RestAccessDeniedHandler;
import com.cmc.meeron.auth.handler.RestAuthenticationEntryPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HealthRestController.class)
class HealthRestControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired WebApplicationContext webApplicationContext;

    @MockBean RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    @MockBean RestAccessDeniedHandler restAccessDeniedHandler;
    @MockBean CustomUserDetailsService customUserDetailsService;

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
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("OK"));
    }
}
