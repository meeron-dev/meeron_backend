package com.cmc.meeron.meeting.application.port.in.response;

import com.cmc.meeron.meeting.domain.Meeting;
import lombok.*;

import java.time.LocalDate;
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
    private LocalDate startDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String meetingName;
    private String purpose;
    private String place;

    private Long workspaceId;
    private String workspaceName;
    private String workspaceLogoUrl;

    public static List<DayMeetingResponseDto> fromEntitiesMeetingWithWorkspace(List<Meeting> meetings) {
        return meetings.stream()
                .map(DayMeetingResponseDto::fromEntityMeetingWithWorkspace)
                .collect(Collectors.toList());
    }

    private static DayMeetingResponseDto fromEntityMeetingWithWorkspace(Meeting meeting) {
        return DayMeetingResponseDto.builder()
                .meetingId(meeting.getId())
                .startDate(meeting.getMeetingTime().getStartDate())
                .startTime(meeting.getMeetingTime().getStartTime())
                .endTime(meeting.getMeetingTime().getEndTime())
                .meetingName(meeting.getMeetingInfo().getName())
                .purpose(meeting.getMeetingInfo().getPurpose())
                .place(meeting.getPlace())
                .workspaceId(meeting.getWorkspace().getId())
                .workspaceName(meeting.getWorkspace().getName())
                .workspaceLogoUrl(meeting.getWorkspace().getWorkspaceLogoUrl())
                .build();
    }

    public static List<DayMeetingResponseDto> fromEntitiesOnlyMeeting(List<Meeting> meetings) {
        return meetings.stream()
                .map(DayMeetingResponseDto::fromEntityOnlyMeeting)
                .collect(Collectors.toList());
    }

    private static DayMeetingResponseDto fromEntityOnlyMeeting(Meeting meeting) {
        return DayMeetingResponseDto.builder()
                .meetingId(meeting.getId())
                .startDate(meeting.getMeetingTime().getStartDate())
                .startTime(meeting.getMeetingTime().getStartTime())
                .endTime(meeting.getMeetingTime().getEndTime())
                .meetingName(meeting.getMeetingInfo().getName())
                .purpose(meeting.getMeetingInfo().getPurpose())
                .place(meeting.getPlace())
                .workspaceId(null)
                .workspaceName(null)
                .workspaceLogoUrl(null)
                .build();
    }
}
