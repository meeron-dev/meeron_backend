package com.cmc.meeron.user.adapter.in;

import com.cmc.meeron.common.security.AuthUser;
import com.cmc.meeron.user.adapter.in.request.SetNameRequest;
import com.cmc.meeron.user.adapter.in.response.UserNamedResponse;
import com.cmc.meeron.user.application.port.in.UserCommandUseCase;
import com.cmc.meeron.user.application.port.in.UserQueryUseCase;
import com.cmc.meeron.user.application.port.in.response.MeResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserRestController {

    private final UserQueryUseCase userQueryUseCase;
    private final UserCommandUseCase userCommandUseCase;

    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public MeResponseDto getUser(@AuthenticationPrincipal AuthUser authUser) {
        return userQueryUseCase.getMe(authUser);
    }

    @GetMapping("/name")
    @ResponseStatus(HttpStatus.OK)
    public UserNamedResponse checkNamedUser(@AuthenticationPrincipal AuthUser authUser) {
        boolean isNamed = userQueryUseCase.checkNamedUser(authUser.getUserId());
        return UserNamedResponse.of(isNamed);
    }

    @PatchMapping("/name")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void setName(@RequestBody @Valid SetNameRequest setNameRequest,
                        @AuthenticationPrincipal AuthUser authUser) {
        userCommandUseCase.setName(authUser, setNameRequest.getName());
    }

    @DeleteMapping("/quit")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void quit(@AuthenticationPrincipal AuthUser authUser) {
        userCommandUseCase.quit(authUser);
    }
}
