package com.cmc.meeron.auth.adapter.in;

import com.cmc.meeron.auth.adapter.in.request.LoginRequest;
import com.cmc.meeron.auth.application.port.in.AuthUseCase;
import com.cmc.meeron.auth.application.port.in.response.TokenResponseDto;
import com.cmc.meeron.common.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthRestController {

    private final AuthUseCase authUseCase;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public TokenResponseDto login(@RequestBody @Valid LoginRequest loginRequest) {
        return authUseCase.login(loginRequest.toRequestDto());
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(@RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken,
                       @RequestHeader("refreshToken") String refreshToken) {
        authUseCase.logout(removeType(accessToken), removeType(refreshToken));
    }

    private String removeType(String token) {
        return token.substring(7);
    }

    @PostMapping("/reissue")
    @ResponseStatus(HttpStatus.OK)
    public TokenResponseDto reissue(@RequestHeader(HttpHeaders.AUTHORIZATION) String refreshToken,
                                    @AuthenticationPrincipal AuthUser authUser) {
        return authUseCase.reissue(removeType(refreshToken), authUser);
    }
}
