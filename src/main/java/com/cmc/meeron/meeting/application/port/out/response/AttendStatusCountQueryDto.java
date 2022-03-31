package com.cmc.meeron.meeting.application.port.out.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

@Getter
@NoArgsConstructor
@Builder
public class AttendStatusCountQueryDto {

    private Long meetingId;
    private String meetingStatus;
    private int count;

    @QueryProjection
    public AttendStatusCountQueryDto(Long meetingId, String meetingStatus, int count) {
        this.meetingId = meetingId;
        this.meetingStatus = meetingStatus;
        this.count = count;
    }
}
