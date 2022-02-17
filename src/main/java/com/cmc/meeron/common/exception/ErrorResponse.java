package com.cmc.meeron.common.exception;

import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponse {

    private LocalDateTime time;
    private int status;
    private String message;
    private String code;
    private List<FieldError> errors;

    private ErrorResponse(LocalDateTime time, int status, String message, String code) {
        this.time = time;
        this.status = status;
        this.message = message;
        this.code = code;
        this.errors = Collections.emptyList();
    }

    // TODO: 2022/02/18 kobeomseok95 이 형태처럼 time, status, message를 외부에서 줄 이유가 없다.
    private ErrorResponse(BindingResult bindingResult) {
        this.time = LocalDateTime.now();
        this.status = HttpStatus.BAD_REQUEST.value();
        this.message = "입력 조건에 대한 예외입니다.";
        this.code = "MEERON-400";
        this.errors = FieldError.of(bindingResult);
    }

    public static ErrorResponse of(int status, String message, String code) {
        return new ErrorResponse(LocalDateTime.now(), status, message, code);
    }

    public static ErrorResponse ofBeanValidation(BindingResult bindingResult) {
        return new ErrorResponse(bindingResult);
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
                            error.getDefaultMessage()
                    ))
                    .collect(Collectors.toList());
        }
    }
}
