package com.cmc.meeron.auth.presentation;

import com.cmc.meeron.auth.application.AuthUseCase;
import com.cmc.meeron.auth.application.dto.response.TokenResponseDto;
import com.cmc.meeron.auth.domain.AuthUser;
import com.cmc.meeron.auth.presentation.dto.request.LoginRequest;
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
        return authUseCase.login(AuthPresentationAssembler.toLoginRequestDto(loginRequest));
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
