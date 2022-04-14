package com.cmc.meeron.attendee.adapter.in.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AttendStatusType {

    ATTEND("ATTEND"),
    ABSENT("ABSENT"),
    ;

    private final String value;
}
