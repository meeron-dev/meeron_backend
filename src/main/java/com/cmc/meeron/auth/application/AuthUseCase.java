package com.cmc.meeron.auth.application;

import com.cmc.meeron.auth.application.dto.request.LoginRequestDto;
import com.cmc.meeron.auth.application.dto.response.TokenResponseDto;

public interface AuthUseCase {

    TokenResponseDto login(LoginRequestDto loginRequestDto);
}
