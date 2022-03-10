package com.cmc.meeron.meeting.application.port.in.response;

import com.cmc.meeron.common.util.LocalDateTimeUtil;
import com.cmc.meeron.meeting.domain.Meeting;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

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

    public static List<WorkspaceAndTeamDayMeetingResponseDto> fromEntities(List<Meeting> dayMeetings) {
        return dayMeetings.stream()
                .map(WorkspaceAndTeamDayMeetingResponseDto::of)
                .collect(Collectors.toList());
    }

    private static WorkspaceAndTeamDayMeetingResponseDto of(Meeting meeting) {
        return WorkspaceAndTeamDayMeetingResponseDto.builder()
                .meetingId(meeting.getId())
                .meetingName(meeting.getName())
                .startTime(LocalDateTimeUtil.convertTime(meeting.getStartTime()))
                .endTime(LocalDateTimeUtil.convertTime(meeting.getEndTime()))
                .build();
    }
}
