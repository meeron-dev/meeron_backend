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
public class TodayMeetingResponseDto {

    private Long meetingId;
    private String meetingName;
    private LocalDate meetingDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Long operationTeamId;
    private String operationTeamName;
    private String meetingStatus;

    public static List<TodayMeetingResponseDto> fromEntities(List<Meeting> todayMeetings) {
        return todayMeetings.stream()
                .map(TodayMeetingResponseDto::of)
                .collect(Collectors.toList());
    }

    private static TodayMeetingResponseDto of(Meeting todayMeeting) {
        return TodayMeetingResponseDto.builder()
                .meetingId(todayMeeting.getId())
                .meetingName(todayMeeting.getName())
                .meetingDate(todayMeeting.getStartDate())
                .startTime(todayMeeting.getStartTime())
                .endTime(todayMeeting.getEndTime())
                .operationTeamId(todayMeeting.getTeam().getId())
                .operationTeamName(todayMeeting.getTeam().getName())
                .meetingStatus(todayMeeting.getMeetingStatus().name())
                .build();
    }
}
