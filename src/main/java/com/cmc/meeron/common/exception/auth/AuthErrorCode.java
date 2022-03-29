package com.cmc.meeron.common.exception.auth;

import com.cmc.meeron.common.exception.ErrorEnumCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuthErrorCode implements ErrorEnumCode {

    UNAUTHENTICATED(1100, "인증되지 않은 회원입니다."),
    EXPIRED(1101, "토큰이 만료되었습니다."),
    NOT_FOUND_REFRESH_TOKEN(1102, "Refresh Token을 찾을 수 없습니다."),
    INVALID_SIGNATURE(1103, "유효하지 않은 서명입니다."),
    INVALID_JWT(1104, "유효하지 않은 토큰입니다."),
    UNSUPPORTED(1105, "지원하지 않는 토큰입니다."),
    EMPTY_CLAIM(1106, "Claim이 존재하지 않습니다."),
    FORBIDDEN(1107, "권한이 필요한 요청입니다."),
    ;

    private final int code;
    private final String message;
}
