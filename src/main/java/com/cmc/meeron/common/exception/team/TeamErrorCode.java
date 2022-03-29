package com.cmc.meeron.common.exception.team;

import com.cmc.meeron.common.exception.ErrorEnumCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TeamErrorCode implements ErrorEnumCode {

    WORKSPACE_IN_TEAM_COUNT_OVER(1400, "워크스페이스 내에서는 다섯 팀까지 보유 가능합니다."),
    PREVIOUS_BELONG_IN_TEAM(1401, "이미 팀이 존재하는 유저가 있습니다."),
    NOT_FOUND_TEAM(1402, "팀을 찾지 못했습니다."),
    ;

    private final int code;
    private final String message;
}
