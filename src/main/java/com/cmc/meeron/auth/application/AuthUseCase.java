package com.cmc.meeron.auth.application;

import com.cmc.meeron.auth.application.dto.request.LoginRequestDto;
import com.cmc.meeron.auth.application.dto.response.TokenResponseDto;
import com.cmc.meeron.auth.domain.AuthUser;

public interface AuthUseCase {

    TokenResponseDto login(LoginRequestDto loginRequestDto);

    void logout(String accessToken, String refreshToken);

    TokenResponseDto reissue(String accessToken, String refreshToken, AuthUser authUser);
}
