package com.cmc.meeron.meeting.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class MeetingInfo {

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 100)
    private String purpose;
}
