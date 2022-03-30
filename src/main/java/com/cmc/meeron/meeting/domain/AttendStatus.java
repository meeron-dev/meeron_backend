package com.cmc.meeron.meeting.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AttendStatus {

    ATTEND("참석"),
    ACCIDENT("갑작스러운 이슈"),
    ABSENT("불참석"),
    UNKNOWN("참석, 불참석 미정"),
    ;

    private final String description;
}
