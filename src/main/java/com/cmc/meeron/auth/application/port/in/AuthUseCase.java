package com.cmc.meeron.auth.application.port.in;

import com.cmc.meeron.auth.application.port.in.request.LoginRequestDto;
import com.cmc.meeron.auth.application.port.in.response.TokenResponseDto;
import com.cmc.meeron.common.security.AuthUser;

public interface AuthUseCase {

    TokenResponseDto login(LoginRequestDto loginRequestDto);

    void logout(String accessToken, String refreshToken);

    TokenResponseDto reissue(String refreshToken, AuthUser authUser);
}
