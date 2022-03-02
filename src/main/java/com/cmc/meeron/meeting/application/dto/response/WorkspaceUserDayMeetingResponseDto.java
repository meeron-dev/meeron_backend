package com.cmc.meeron.meeting.application.dto.response;

import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkspaceUserDayMeetingResponseDto {

    private Long meetingId;
    private String meetingName;
    private String startTime;
    private String endTime;
    private Long workspaceId;
    private String workspaceName;
}
