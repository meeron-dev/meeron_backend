package com.cmc.meeron.auth.presentation;

import com.cmc.meeron.auth.application.dto.response.TokenResponseDto;
import com.cmc.meeron.auth.domain.AuthUser;
import com.cmc.meeron.auth.presentation.dto.request.LoginRequest;
import com.cmc.meeron.support.restdocs.RestDocsTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;

import static com.cmc.meeron.config.RestDocsConfig.field;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthRestControllerTest extends RestDocsTestSupport {

    @DisplayName("로그인 - 성공")
    @Test
    void login_success() throws Exception {

        // given
        LoginRequest request = LoginRequest.builder()
                .email("test@naver.com")
                .nickname("고범석")
                .profileImageUrl("https://test.image.com/12341234")
                .provider("kakao")
                .build();
        when(authUseCase.login(any()))
                .thenReturn(mockJwt());

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type", is("Bearer")))
                .andExpect(jsonPath("$.accessToken", is("testAccessToken")))
                .andExpect(jsonPath("$.refreshToken", is("testRefreshToken")))
                .andDo(restDocumentationResultHandler.document(
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING).description("회원가입 / 로그인 할 이메일").attributes(field("constraints", "Email 형식이어야 함")),
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("소셜 닉네임").optional(),
                                fieldWithPath("profileImageUrl").type(JsonFieldType.STRING).description("소셜 로그인할 때 프로필 이미지 URL").optional(),
                                fieldWithPath("provider").type(JsonFieldType.STRING).description("소셜 로그인 제공자").attributes(field("constraints", "KAKAO, APPLE 만 가능"))
                        ),
                        responseFields(
                                fieldWithPath("type").type(JsonFieldType.STRING).description("토큰 타입 Bearer 고정"),
                                fieldWithPath("accessToken").type(JsonFieldType.STRING).description("JWT Access Token"),
                                fieldWithPath("refreshToken").type(JsonFieldType.STRING).description("JWT Refresh Token")
                        )
                ));
    }

    private TokenResponseDto mockJwt() {
        return TokenResponseDto.of("testAccessToken", "testRefreshToken");
    }

    @DisplayName("로그인 - 실패 / 입력 조건을 지키지 않을 경우")
    @Test
    void login_fail_not_valid() throws Exception {

        // given
        LoginRequest request = LoginRequest.builder()
                .email(null)
                .nickname(null)
                .profileImageUrl("https://test.image.com/12341234")
                .provider(null)
                .build();

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(2)));
    }

    @DisplayName("로그인 - 실패 / 카카오, 애플이 아닌 provider는 소셜 로그인이 제공되지 않는다.")
    @Test
    void login_fail_not_provider() throws Exception {

        // given
        LoginRequest request = LoginRequest.builder()
                .email("test@naver.com")
                .nickname(null)
                .profileImageUrl(null)
                .provider("naver")
                .build();

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(1)));
    }

    @DisplayName("로그아웃 - 성공")
    @Test
    void logout_success() throws Exception {

        // given
        setUpAuthenticated();
        String accessToken = "Bearer testAccessToken";
        String refreshToken = "Bearer testRefreshToken";

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/logout")
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .header("refreshToken", refreshToken))
                .andExpect(status().isNoContent())
                .andDo(restDocumentationResultHandler.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access Token").attributes(field("constraints", "JWT Access Token With Bearer")),
                                headerWithName("refreshToken").description("JWT Refresh Token").attributes(field("constraints", "JWT Refresh Token With Bearer"))
                        )
                ));
    }

    @DisplayName("토큰 재발급 - 성공")
    @Test
    void reissue_success() throws Exception {

        // given
        setUpAuthenticated();
        String accessToken = "Bearer testAccessToken";
        String refreshToken = "Bearer testRefreshToken";
        when(authUseCase.reissue(any(), any(), any(AuthUser.class)))
                .thenReturn(mockJwt());

        // when, then, docs
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/reissue")
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .header("refreshToken", refreshToken))
                .andExpect(status().isOk())
                .andDo(restDocumentationResultHandler.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access Token").attributes(field("constraints", "JWT Access Token With Bearer")),
                                headerWithName("refreshToken").description("JWT Refresh Token").attributes(field("constraints", "JWT Refresh Token With Bearer"))
                        ),
                        responseFields(
                                fieldWithPath("type").type(JsonFieldType.STRING).description("토큰 타입 Bearer 고정"),
                                fieldWithPath("accessToken").type(JsonFieldType.STRING).description("JWT Access Token"),
                                fieldWithPath("refreshToken").type(JsonFieldType.STRING).description("JWT Refresh Token")
                        )
                ));
    }
}
