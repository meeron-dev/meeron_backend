package com.cmc.meeron.meeting.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MeetingStatus {

    EXPECT("회의 예정"),
    END("회의 종료"),
    ;

    private final String description;
}
