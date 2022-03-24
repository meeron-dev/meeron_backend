package com.cmc.meeron.meeting.adapter.in.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AttendStatusType {

    ATTEND("ATTEND"),
    ACCIDENT("ACCIDENT"),
    ABSENT("ABSENT"),
    ;

    private final String value;
}
