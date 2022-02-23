package com.cmc.meeron.meeting.application.dto.response;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TodayMeetingResponseDto {

    private Long meetingId;
    private String meetingName;
    private LocalDate meetingDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Long operationTeamId;
    private String operationTeamName;
    private String meetingStatus;
}
