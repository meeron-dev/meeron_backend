package com.cmc.meeron.auth.presentation;

import com.cmc.meeron.auth.application.AuthUseCase;
import com.cmc.meeron.auth.application.dto.request.LoginRequestDto;
import com.cmc.meeron.auth.application.dto.response.TokenResponseDto;
import com.cmc.meeron.auth.presentation.dto.request.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
