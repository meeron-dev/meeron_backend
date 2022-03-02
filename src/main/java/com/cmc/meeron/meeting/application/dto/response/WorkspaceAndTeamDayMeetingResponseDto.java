package com.cmc.meeron.meeting.application.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkspaceAndTeamDayMeetingResponseDto {

    private Long meetingId;
    private String meetingName;
    private String startTime;
    private String endTime;
}
