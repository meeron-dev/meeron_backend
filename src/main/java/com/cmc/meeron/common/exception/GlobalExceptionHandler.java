package com.cmc.meeron.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> applicationException(ApplicationException e) {
        log.error(e.getMessage());
        return ResponseEntity.badRequest().body(ErrorResponse.fromApplicationCommonException(e.getMessage()));
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> bindException(BindException e) {
        log.error(e.getMessage());
        return ResponseEntity.badRequest().body(ErrorResponse.fromBeanValidation(e.getBindingResult()));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> maxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        log.error(e.getMessage());
        return ResponseEntity.badRequest().body(ErrorResponse
                .fromApplicationCommonException("파일 크기가 초과되었습니다. 10MB 내로 파일을 업로드해주세요."));
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<ErrorResponse> maxUploadSizeExceededException(MissingServletRequestPartException e) {
        log.error(e.getMessage());
        return ResponseEntity.badRequest().body(ErrorResponse
                .fromApplicationCommonException("파일을 하나 이상 보내야 합니다."));
    }
}
