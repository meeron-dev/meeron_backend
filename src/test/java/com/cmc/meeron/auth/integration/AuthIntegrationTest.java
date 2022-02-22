package com.cmc.meeron.auth.integration;

import com.cmc.meeron.auth.application.dto.response.TokenResponseDto;
import com.cmc.meeron.auth.domain.AuthUser;
import com.cmc.meeron.auth.domain.repository.LogoutAccessTokenRepository;
import com.cmc.meeron.auth.domain.repository.LogoutRefreshTokenRepository;
import com.cmc.meeron.auth.domain.repository.RefreshTokenRepository;
import com.cmc.meeron.auth.presentation.dto.request.LoginRequest;
import com.cmc.meeron.auth.provider.JwtProvider;
import com.cmc.meeron.support.IntegrationTest;
import com.cmc.meeron.support.security.WithMockJwt;
import com.cmc.meeron.user.domain.User;
import com.cmc.meeron.user.domain.repository.UserRepository;
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
    @Autowired RefreshTokenRepository refreshTokenRepository;
    @Autowired JwtProvider jwtProvider;
    @Autowired LogoutAccessTokenRepository logoutAccessTokenRepository;
    @Autowired LogoutRefreshTokenRepository logoutRefreshTokenRepository;

    private User createMockUser() {
        return User.of("test@naver.com", "test", "KAKAO");
    }

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
                () -> assertTrue(refreshTokenRepository.existsById(request.getEmail()))
        );
    }

    private static Stream<Arguments> provideLoginUser() {
        return Stream.of(
                Arguments.of(LoginRequest.builder()
                        .email("test@naver.com")
                        .nickname("고범석")
                        .profileImageUrl("https://test.image.com/12341234")
                        .provider("kakao")
                        .build()),
                Arguments.of(LoginRequest.builder()
                        .email("test@apple.com")
                        .nickname(null)
                        .profileImageUrl(null)
                        .provider("apple")
                        .build())
        );
    }

    @WithMockJwt
    @DisplayName("로그아웃 - 성공")
    @Test
    void logout_success() throws Exception {

        // given
        User user = setUpMockUser();
        String accessToken = "Bearer " + jwtProvider.createAccessToken(AuthUser.of(user));
        String refreshToken = "Bearer " + jwtProvider.createRefreshToken(AuthUser.of(user));

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/logout")
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .header("refreshToken", refreshToken))
                .andExpect(status().isNoContent());

        // then
        assertAll(
                () -> assertTrue(logoutAccessTokenRepository.existsById(accessToken.substring(7))),
                () -> assertTrue(logoutRefreshTokenRepository.existsById(refreshToken.substring(7)))
        );
    }

    private User setUpMockUser() {
        return userRepository.save(createMockUser());
    }

    @WithMockJwt
    @DisplayName("토큰 재발급 - 성공")
    @Test
    void reissue_success() throws Exception {

        // given
        User user = setUpMockUser();
        String accessToken = "Bearer " + jwtProvider.createAccessToken(AuthUser.of(user));
        String refreshToken = "Bearer " + jwtProvider.createRefreshToken(AuthUser.of(user));

        // when
        String response = mockMvc.perform(MockMvcRequestBuilders.post("/api/reissue")
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .header("refreshToken", refreshToken))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        TokenResponseDto responseDto = objectMapper.readValue(response, TokenResponseDto.class);

        // then
        assertAll(
                () -> assertNotEquals(accessToken.substring(7), responseDto.getAccessToken()),
                () -> assertEquals(refreshToken.substring(7), responseDto.getRefreshToken()),
                () -> assertTrue(refreshTokenRepository.existsById(user.getEmail()))
        );
    }
}
