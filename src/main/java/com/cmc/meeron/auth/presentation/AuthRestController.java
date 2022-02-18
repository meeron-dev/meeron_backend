package com.cmc.meeron.auth.presentation;

import com.cmc.meeron.auth.application.AuthUseCase;
import com.cmc.meeron.auth.application.dto.request.LoginRequestDto;
import com.cmc.meeron.auth.application.dto.response.TokenResponseDto;
import com.cmc.meeron.auth.presentation.dto.request.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthRestController {

    private final AuthUseCase authUseCase;

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@RequestBody @Valid LoginRequest loginRequest) {
        LoginRequestDto loginRequestDto = AuthPresentationAssembler.toLoginRequestDto(loginRequest);
        TokenResponseDto tokenResponseDto = authUseCase.login(loginRequestDto);
        return ResponseEntity.ok(tokenResponseDto);
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
}
