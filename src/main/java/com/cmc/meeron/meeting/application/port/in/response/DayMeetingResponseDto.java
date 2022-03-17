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
public class DayMeetingResponseDto {

    private Long meetingId;
    private String meetingName;
    private LocalTime startTime;
    private LocalTime endTime;
    private Long workspaceId;
    private String workspaceName;

    public static List<DayMeetingResponseDto> fromMyMeetingEntities(List<Meeting> meetings) {
        return meetings.stream()
                .map(DayMeetingResponseDto::ofMyDayMeetings)
                .collect(Collectors.toList());
    }

    private static DayMeetingResponseDto ofMyDayMeetings(Meeting meeting) {
        return DayMeetingResponseDto.builder()
                .meetingId(meeting.getId())
                .meetingName(meeting.getMeetingInfo().getName())
                .startTime(meeting.getMeetingTime().getStartTime())
                .endTime(meeting.getMeetingTime().getEndTime())
                .workspaceId(meeting.getWorkspace().getId())
                .workspaceName(meeting.getWorkspace().getName())
                .build();
    }

    public static List<DayMeetingResponseDto> fromEntities(List<Meeting> meetings) {
        return meetings.stream()
                .map(DayMeetingResponseDto::of)
                .collect(Collectors.toList());
    }

    private static DayMeetingResponseDto of(Meeting meeting) {
        return DayMeetingResponseDto.builder()
                .meetingId(meeting.getId())
                .meetingName(meeting.getMeetingInfo().getName())
                .startTime(meeting.getMeetingTime().getStartTime())
                .endTime(meeting.getMeetingTime().getEndTime())
                .workspaceId(0L)
                .workspaceName("")
                .build();
    }
}
