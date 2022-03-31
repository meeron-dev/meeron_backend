package com.cmc.meeron.meeting.application.port.out.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
@Builder
public class MeetingQueryDto {

    private Long meetingId;
    private String meetingName;
    private String meetingPurpose;
    private LocalDate meetingDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Long operationTeamId;
    private String operationTeamName;

    @QueryProjection
    public MeetingQueryDto(Long meetingId, String meetingName, String meetingPurpose, LocalDate meetingDate, LocalTime startTime, LocalTime endTime, Long operationTeamId, String operationTeamName) {
        this.meetingId = meetingId;
        this.meetingName = meetingName;
        this.meetingPurpose = meetingPurpose;
        this.meetingDate = meetingDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.operationTeamId = operationTeamId;
        this.operationTeamName = operationTeamName;
    }
}
