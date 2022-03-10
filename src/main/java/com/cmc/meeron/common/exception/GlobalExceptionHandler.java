package com.cmc.meeron.common.exception;

import com.cmc.meeron.common.exception.file.FileUploadException;
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
        return ResponseEntity.badRequest().body(ErrorResponse.of(e));
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> bindException(BindException e) {
        log.error(e.getMessage());
        return ResponseEntity.badRequest().body(ErrorResponse.fromBindException(e.getBindingResult()));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> maxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        log.error(e.getMessage());
        return ResponseEntity.badRequest().body(ErrorResponse
                .of(new FileUploadException("파일 용량을 초과했습니다.")));
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<ErrorResponse> maxUploadSizeExceededException(MissingServletRequestPartException e) {
        log.error(e.getMessage());
        return ResponseEntity.badRequest().body(ErrorResponse
                .of(new FileUploadException("파일을 최소 하나 이상 보내야합니다.")));
    }
}
