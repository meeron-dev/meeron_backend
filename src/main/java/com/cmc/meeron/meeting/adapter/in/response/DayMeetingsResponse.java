package com.cmc.meeron.meeting.adapter.in.response;

import com.cmc.meeron.meeting.application.port.in.response.WorkspaceAndTeamDayMeetingResponseDto;
import com.cmc.meeron.meeting.application.port.in.response.WorkspaceUserDayMeetingResponseDto;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DayMeetingsResponse {

    @Builder.Default
    private List<DayMeeting> meetings = new ArrayList<>();

    public static DayMeetingsResponse fromWorkspaceAndTeam(List<WorkspaceAndTeamDayMeetingResponseDto> responseDtos) {
        return DayMeetingsResponse.builder()
                .meetings(responseDtos.stream()
                        .map(dto -> DayMeeting.builder()
                                .meetingId(dto.getMeetingId())
                                .meetingName(dto.getMeetingName())
                                .startTime(dto.getStartTime())
                                .endTime(dto.getEndTime())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    public static DayMeetingsResponse fromWorkspaceUser(List<WorkspaceUserDayMeetingResponseDto> responseDtos) {
        return DayMeetingsResponse.builder()
                .meetings(responseDtos.stream()
                        .map(dto -> DayMeeting.builder()
                                .meetingId(dto.getMeetingId())
                                .meetingName(dto.getMeetingName())
                                .startTime(dto.getStartTime())
                                .endTime(dto.getEndTime())
                                .workspaceId(dto.getWorkspaceId())
                                .workspaceName(dto.getWorkspaceName())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DayMeeting {

        private Long meetingId;
        private String meetingName;
        private String startTime;
        private String endTime;
        private Long workspaceId;
        private String workspaceName;
    }
}
