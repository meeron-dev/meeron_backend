package com.cmc.meeron.meeting.domain;

import lombok.*;

@Value
@EqualsAndHashCode(of = {"name", "purpose"})
@Builder
public class MeetingBasicInfoVo {

    private final String name;
    private final String purpose;
}
