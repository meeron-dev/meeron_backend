package com.cmc.meeron.auth.integration;

import com.cmc.meeron.auth.application.port.in.response.TokenResponseDto;
import com.cmc.meeron.auth.application.port.out.TokenQueryPort;
import com.cmc.meeron.auth.adapter.in.request.LoginRequest;
import com.cmc.meeron.common.security.JwtProvider;
import com.cmc.meeron.support.IntegrationTest;
import com.cmc.meeron.user.application.port.out.UserCommandPort;
import com.cmc.meeron.user.domain.User;
import com.cmc.meeron.user.application.port.out.UserQueryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthIntegrationTest extends IntegrationTest {

    @Autowired UserQueryPort userQueryPort;
    @Autowired TokenQueryPort tokenQueryPort;
    @Autowired UserCommandPort userCommandPort;
    @Autowired JwtProvider jwtProvider;

    @DisplayName("로그인 - 성공 / 카카오, 애플 로그인")
    @ParameterizedTest
    @MethodSource("provideLoginUser")
    void login_success(LoginRequest request) throws Exception {

        // given, when, then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        assertAll(
                () -> assertTrue(userQueryPort.findByEmail(request.getEmail()).isPresent()),
                () -> assertTrue(tokenQueryPort.existsRefreshTokenByUsername(request.getEmail()))
        );
    }

    private static Stream<Arguments> provideLoginUser() {
        return Stream.of(
                Arguments.of(createLoginRequest()),
                Arguments.of(LoginRequest.builder()
                        .email("test@apple.com")
                        .nickname(null)
                        .profileImageUrl(null)
                        .provider("apple")
                        .build())
        );
    }

    private static LoginRequest createLoginRequest() {
        return LoginRequest.builder()
                .email("test@naver.com")
                .nickname("고범석")
                .profileImageUrl("https://test.image.com/12341234")
                .provider("KAKAO")
                .build();
    }

    @DisplayName("로그아웃 - 성공")
    @Test
    void logout_success() throws Exception {

        // given
        LoginRequest loginRequest = createLoginRequest();
        setUpMockUser(loginRequest);
        TokenResponseDto loggedIn = login(loginRequest);

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/logout")
                .header(HttpHeaders.AUTHORIZATION, loggedIn.getType() + " " + loggedIn.getAccessToken())
                .header("refreshToken", loggedIn.getType() + " " + loggedIn.getRefreshToken()))
                .andExpect(status().isNoContent());

        // then
        assertAll(
                () -> assertTrue(tokenQueryPort.existsLogoutAccessTokenById(loggedIn.getAccessToken())),
                () -> assertTrue(tokenQueryPort.existsLogoutRefreshTokenById(loggedIn.getRefreshToken()))
        );
    }

    private User setUpMockUser(LoginRequest loginRequest) {
        return userCommandPort.save(createMockUser(loginRequest));
    }

    private User createMockUser(LoginRequest loginRequest) {
        return User.of(loginRequest.getEmail(),
                loginRequest.getNickname(),
                loginRequest.getProvider());
    }

    private TokenResponseDto login(LoginRequest loginRequest) throws Exception {
        String content = mockMvc.perform(MockMvcRequestBuilders.post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andReturn()
                .getResponse()
                .getContentAsString();
        return objectMapper.readValue(content, TokenResponseDto.class);
    }

    @DisplayName("토큰 재발급 - 성공")
    @Test
    void reissue_success() throws Exception {

        // given
        LoginRequest loginRequest = createLoginRequest();
        User user = setUpMockUser(loginRequest);
        TokenResponseDto loggedIn = login(loginRequest);

        // when
        String response = mockMvc.perform(MockMvcRequestBuilders.post("/api/reissue")
                .header(HttpHeaders.AUTHORIZATION, loggedIn.getType() + " " + loggedIn.getRefreshToken()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        TokenResponseDto responseToken = objectMapper.readValue(response, TokenResponseDto.class);

        // then
        assertAll(
                () -> assertTrue(tokenQueryPort.existsRefreshTokenByUsername(user.getEmail())),
                () -> assertEquals(responseToken.getRefreshToken(), loggedIn.getRefreshToken())
        );
    }
}
