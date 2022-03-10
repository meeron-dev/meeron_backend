package com.cmc.meeron.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApplicationException extends RuntimeException {

    private ErrorEnumCode errorEnumCode;

    public ApplicationException(ErrorEnumCode errorEnumCode, String message) {
        super(message);
        this.errorEnumCode = errorEnumCode;
    }
}
