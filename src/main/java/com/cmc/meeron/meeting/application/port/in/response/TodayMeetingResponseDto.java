package com.cmc.meeron.meeting.application.port.in.response;

import com.cmc.meeron.meeting.application.port.out.response.AttendStatusCountResponseDto;
import com.cmc.meeron.meeting.domain.AttendStatus;
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
    private int attends;
    private int unknowns;
    private int absents;

    public static List<TodayMeetingResponseDto> fromEntitiesAndCounts(List<Meeting> todayMeetings,
                                                                      List<AttendStatusCountResponseDto> attendStatusCountResponseDtos) {


        List<TodayMeetingResponseDto> todayMeetingResponseDtos = todayMeetings.stream()
                .map(TodayMeetingResponseDto::of)
                .collect(Collectors.toList());
        attendStatusCountResponseDtos.forEach(attendStatusCountDto -> {
            todayMeetingResponseDtos.stream()
                    .filter(todayMeetingResponseDto -> todayMeetingResponseDto.getMeetingId().equals(attendStatusCountDto.getMeetingId()))
                    .findFirst()
                    .ifPresent(todayMeetingResponseDto -> todayMeetingResponseDto.setCount(attendStatusCountDto));
        });
        return todayMeetingResponseDtos;
    }

    private static TodayMeetingResponseDto of(Meeting todayMeeting) {
        return TodayMeetingResponseDto.builder()
                .meetingId(todayMeeting.getId())
                .meetingName(todayMeeting.getMeetingInfo().getName())
                .meetingDate(todayMeeting.getMeetingTime().getStartDate())
                .startTime(todayMeeting.getMeetingTime().getStartTime())
                .endTime(todayMeeting.getMeetingTime().getEndTime())
                .operationTeamId(todayMeeting.getTeam().getId())
                .operationTeamName(todayMeeting.getTeam().getName())
                .build();
    }

    private void setCount(AttendStatusCountResponseDto attendStatusCountResponseDto) {
        if (attendStatusCountResponseDto.getMeetingStatus().equals(AttendStatus.ATTEND.name())) {
            attends = attendStatusCountResponseDto.getCount();
        } else if (attendStatusCountResponseDto.getMeetingStatus().equals(AttendStatus.ABSENT.name())) {
            absents = attendStatusCountResponseDto.getCount();
        } else {
            unknowns = attendStatusCountResponseDto.getCount();
        }
    }
}
