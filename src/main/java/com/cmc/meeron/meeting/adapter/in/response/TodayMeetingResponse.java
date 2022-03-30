package com.cmc.meeron.meeting.adapter.in.response;

import com.cmc.meeron.meeting.application.port.in.response.TodayMeetingResponseDto;
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
public class TodayMeetingResponse {

    private List<TodayMeeting> meetings;

    public static TodayMeetingResponse fromDto(List<TodayMeetingResponseDto> todayExpectedMeetings) {
        return TodayMeetingResponse.builder()
                .meetings(todayExpectedMeetings.stream()
                        .map(meeting -> TodayMeeting.builder()
                                .meetingId(meeting.getMeetingId())
                                .meetingName(meeting.getMeetingName())
                                .meetingDate(meeting.getMeetingDate())
                                .startTime(meeting.getStartTime())
                                .endTime(meeting.getEndTime())
                                .operationTeamId(meeting.getOperationTeamId())
                                .operationTeamName(meeting.getOperationTeamName())
                                .attends(meeting.getAttends())
                                .absents(meeting.getAbsents())
                                .unknowns(meeting.getUnknowns())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TodayMeeting {
        private Long meetingId;
        private String meetingName;
        private LocalDate meetingDate;
        private LocalTime startTime;
        private LocalTime endTime;
        private Long operationTeamId;
        private String operationTeamName;
        private int attends;
        private int absents;
        private int unknowns;
    }
}
