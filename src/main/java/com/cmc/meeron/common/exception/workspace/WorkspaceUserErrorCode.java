package com.cmc.meeron.common.exception.workspace;

import com.cmc.meeron.common.exception.ErrorEnumCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WorkspaceUserErrorCode implements ErrorEnumCode {

    DUPLICATE_NICKNAME(1700, "한 워크스페이스 내에 닉네임은 중복될 수 없습니다."),
    NOT_FOUND(1701, "워크스페이스 유저를 찾을 수 없습니다."),
    ONLY_ADMIN(1702, "해당 기능은 워크스페이스 관리자만 실행할 수 있습니다."),
    NOT_BELONG_TO_WORKSPACE(1703, "워크스페이스 내에 속하지 않은 유저가 있습니다."),
    NOT_ALL_FOUND(1704, "워크스페이스 유저를 다 찾지 못했습니다."),
    ;

    private final int code;
    private final String message;
}
