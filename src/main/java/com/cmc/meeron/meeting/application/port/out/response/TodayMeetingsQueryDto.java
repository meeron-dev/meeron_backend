package com.cmc.meeron.meeting.application.port.out.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
@Builder
public class TodayMeetingsQueryDto {

    private Long meetingId;
    private String meetingName;
    private String meetingPurpose;
    private LocalDate meetingDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Long operationTeamId;
    private String operationTeamName;
    private String agendaContent;

    @QueryProjection
    public TodayMeetingsQueryDto(Long meetingId, String meetingName, String meetingPurpose, LocalDate meetingDate, LocalTime startTime, LocalTime endTime, Long operationTeamId, String operationTeamName, String agendaContent) {
        this.meetingId = meetingId;
        this.meetingName = meetingName;
        this.meetingPurpose = meetingPurpose;
        this.meetingDate = meetingDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.operationTeamId = operationTeamId;
        this.operationTeamName = operationTeamName;
        this.agendaContent = agendaContent;
    }
}
