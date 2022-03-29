package com.cmc.meeron.common.exception;

import com.cmc.meeron.common.exception.auth.AuthErrorCode;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.cmc.meeron.common.exception.ClientErrorCode.TYPE_MISMATCH_EXCEPTION;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponse {

    private LocalDateTime time;
    private int status;
    private String message;
    private int code;
    private List<FieldError> errors;

    private ErrorResponse(BindingResult bindingResult) {
        this.time = LocalDateTime.now();
        this.status = HttpStatus.BAD_REQUEST.value();
        this.message = "입력 조건에 대한 예외입니다.";
        this.code = ClientErrorCode.BIND_EXCEPTION.getCode();
        this.errors = FieldError.of(bindingResult);
    }

    private ErrorResponse(ApplicationException e) {
        this.time = LocalDateTime.now();
        this.status = HttpStatus.BAD_REQUEST.value();
        this.message = e.getMessage();
        this.code = e.getErrorEnumCode().getCode();
        this.errors = Collections.emptyList();
    }

    private ErrorResponse(int status, String message, ErrorEnumCode errorEnumCode) {
        this.time = LocalDateTime.now();
        this.status = status;
        this.message = message;
        this.code = errorEnumCode.getCode();
        this.errors = Collections.emptyList();
    }

    public static ErrorResponse fromBindException(BindingResult bindingResult) {
        return new ErrorResponse(bindingResult);
    }

    public static ErrorResponse of(ApplicationException e) {
        return new ErrorResponse(e);
    }

    public static ErrorResponse fromUnauthorizedAtFilter(String message) {
        return new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), message, AuthErrorCode.UNAUTHENTICATED);
    }

    public static ErrorResponse fromTokenAuthenticationFilter(ApplicationException e) {
        return new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), e.getMessage(), e.getErrorEnumCode());
    }

    public static ErrorResponse fromForbiddenAtFilter(String message) {
        return new ErrorResponse(HttpStatus.FORBIDDEN.value(), message, AuthErrorCode.FORBIDDEN);
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Builder
    public static class FieldError {

        private String field;
        private String value;
        private String reason;

        private FieldError(String field, String value, String reason) {
            this.field = field;
            this.value = value;
            this.reason = reason;
        }

        private static List<FieldError> of(BindingResult bindingResult) {
            List<org.springframework.validation.FieldError> fieldErrors = bindingResult.getFieldErrors();
            return fieldErrors.stream()
                    .map(error -> new FieldError(
                            error.getField(),
                            error.getRejectedValue() == null ? null : error.getRejectedValue().toString(),
                            Objects.equals(error.getCode(), "typeMismatch")
                                    ? TYPE_MISMATCH_EXCEPTION.getMessage()
                                    : error.getDefaultMessage()
                    ))
                    .collect(Collectors.toList());
        }
    }
}
