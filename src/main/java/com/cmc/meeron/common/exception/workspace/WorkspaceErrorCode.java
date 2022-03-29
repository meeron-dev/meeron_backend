package com.cmc.meeron.common.exception.workspace;

import com.cmc.meeron.common.exception.ErrorEnumCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WorkspaceErrorCode implements ErrorEnumCode {

    NOT_FOUND(1600, "워크스페이스를 찾지 못했습니다."),
    ;

    private final int code;
    private final String message;
}
