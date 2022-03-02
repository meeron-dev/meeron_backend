package com.cmc.meeron.auth.integration;

import com.cmc.meeron.auth.application.dto.response.TokenResponseDto;
import com.cmc.meeron.auth.domain.TokenRepository;
import com.cmc.meeron.auth.presentation.dto.request.LoginRequest;
import com.cmc.meeron.auth.provider.JwtProvider;
import com.cmc.meeron.support.IntegrationTest;
import com.cmc.meeron.user.domain.User;
import com.cmc.meeron.user.domain.UserRepository;
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

    @Autowired UserRepository userRepository;
    @Autowired TokenRepository tokenRepository;
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
                () -> assertTrue(userRepository.findByEmail(request.getEmail()).isPresent()),
                () -> assertTrue(tokenRepository.existsRefreshTokenByUsername(request.getEmail()))
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
                () -> assertTrue(tokenRepository.existsLogoutAccessTokenById(loggedIn.getAccessToken())),
                () -> assertTrue(tokenRepository.existsLogoutRefreshTokenById(loggedIn.getRefreshToken()))
        );
    }

    private User setUpMockUser(LoginRequest loginRequest) {
        return userRepository.save(createMockUser(loginRequest));
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
                () -> assertTrue(tokenRepository.existsRefreshTokenByUsername(user.getEmail())),
                () -> assertEquals(responseToken.getRefreshToken(), loggedIn.getRefreshToken())
        );
    }
}
