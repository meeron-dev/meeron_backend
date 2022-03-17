package com.cmc.meeron.meeting.application.port.in.response;

import com.cmc.meeron.meeting.domain.Meeting;
import lombok.*;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkspaceUserDayMeetingResponseDto {

    private Long meetingId;
    private String meetingName;
    private LocalTime startTime;
    private LocalTime endTime;
    private Long workspaceId;
    private String workspaceName;

    public static List<WorkspaceUserDayMeetingResponseDto> fromEntities(List<Meeting> meetings) {
        return meetings.stream()
                .map(WorkspaceUserDayMeetingResponseDto::of)
                .collect(Collectors.toList());
    }

    private static WorkspaceUserDayMeetingResponseDto of(Meeting meeting) {
        return WorkspaceUserDayMeetingResponseDto.builder()
                .meetingId(meeting.getId())
                .meetingName(meeting.getMeetingInfo().getName())
                .startTime(meeting.getMeetingTime().getStartTime())
                .endTime(meeting.getMeetingTime().getEndTime())
                .workspaceId(meeting.getWorkspace().getId())
                .workspaceName(meeting.getWorkspace().getName())
                .build();
    }
}
