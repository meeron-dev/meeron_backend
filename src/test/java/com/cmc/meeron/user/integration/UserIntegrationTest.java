package com.cmc.meeron.user.integration;

import com.cmc.meeron.auth.application.port.in.AuthUseCase;
import com.cmc.meeron.auth.application.port.in.request.LoginRequestDto;
import com.cmc.meeron.auth.application.port.in.response.TokenResponseDto;
import com.cmc.meeron.support.IntegrationTest;
import com.cmc.meeron.support.security.WithMockJwt;
import com.cmc.meeron.user.adapter.in.request.SetNameRequest;
import com.cmc.meeron.user.application.port.out.UserQueryPort;
import com.cmc.meeron.user.domain.User;
import com.google.common.net.HttpHeaders;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserIntegrationTest extends IntegrationTest {

    @Autowired AuthUseCase authUseCase;
    @Autowired UserQueryPort userQueryPort;

    @DisplayName("내 정보 조회 - 성공")
    @Test
    void get_me_success() throws Exception {

        // given
        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
                .email("test1@test.com")
                .provider("KAKAO")
                .build();
        TokenResponseDto token = authUseCase.login(loginRequestDto);

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/me")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", is(1)))
                .andExpect(jsonPath("$.loginEmail", is(loginRequestDto.getEmail())));
    }

    @WithMockJwt
    @DisplayName("유저의 성함 등록 - 성공")
    @Test
    void set_name_success() throws Exception {

        // given
        SetNameRequest request = createSetNameRequest();

        // when
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/users/name")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // then
        flushAndClear();
        User user = userQueryPort.findByEmail("test1@test.com").orElseThrow();
        assertEquals(request.getName(), user.getName());
    }

    private SetNameRequest createSetNameRequest() {
        return SetNameRequest.builder()
                .name("테스트")
                .build();
    }

    @Sql("classpath:user-test.sql")
    @WithMockJwt(id = 6L, email = "test6@test.com")
    @DisplayName("유저가 성함을 입력했는지 검증 - 성공 / 입력했을 경우")
    @Test
    void check_named_user_success_named() throws Exception {

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/name")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.named", is(true)));
    }

    @WithMockJwt
    @DisplayName("유저가 성함을 입력했는지 검증 - 성공 / 입력하지 않았을 경우")
    @Test
    void check_named_user_success_not_named() throws Exception {

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/name")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.named", is(false)));
    }
}
