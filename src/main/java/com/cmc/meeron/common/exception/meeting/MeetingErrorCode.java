package com.cmc.meeron.common.exception.meeting;

import com.cmc.meeron.common.exception.ErrorEnumCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MeetingErrorCode implements ErrorEnumCode {

    NOT_FOUND_AGENDA(1300, "아젠다를 찾을 수 없습니다."),
    ATTENDEE_DUPLICATE(1301, "이미 참여중인 유저가 있습니다."),
    NOT_FOUND_ATTENDEE(1302, "회의 참가자를 찾을 수 없습니다."),
    NOT_FOUND_MEETING(1303, "회의를 찾을 수 없습니다."),
    OPERATION_TEAM_IS_NOT_IN_WORKSPACE(1304, "주관하는 팀이 지정한 워크스페이스에 속해있지 않습니다."),
    NOT_ALLOW_SEARCH_TYPE(1305, "검색 유형은 'WORKSPACE', 'WORKSPACE_USER', 'TEAM' 중 하나를 입력해주세요.")
    ;

    private final int code;
    private final String message;
}
