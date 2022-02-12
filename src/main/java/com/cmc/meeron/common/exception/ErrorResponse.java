package com.cmc.meeron.common.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponse {

    private LocalDateTime time;
    private int status;
    private String message;
    private String code;

    public static ErrorResponse of(int status, String message, String code) {
        return new ErrorResponse(LocalDateTime.now(), status, message, code);
    }
}
