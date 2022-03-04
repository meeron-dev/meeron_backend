package com.cmc.meeron.meeting.adapter.in.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MeetingDaysSearchType {

    WORKSPACE("WORKSPACE"),
    WORKSPACE_USER("WORKSPACE_USER"),
    TEAM("TEAM"),
    ;

    private final String value;
}
