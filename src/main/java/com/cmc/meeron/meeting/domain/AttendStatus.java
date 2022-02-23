package com.cmc.meeron.meeting.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AttendStatus {

    ATTEND("참석"),
    SURPRISE("갑작스러운 불참"),
    ABSENT("불참석"),
    ;

    private final String description;
}
