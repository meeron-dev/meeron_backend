package com.cmc.meeron.meeting.application.port.in.response;

import com.cmc.meeron.meeting.application.port.out.response.AttendStatusCountQueryDto;
import com.cmc.meeron.meeting.application.port.out.response.TodayMeetingsQueryDto;
import com.cmc.meeron.meeting.domain.AttendStatus;
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
    private String agendaContent;
    private int attends;
    private int unknowns;
    private int absents;

    public static List<TodayMeetingResponseDto> fromQueryDtos(List<TodayMeetingsQueryDto> todayMeetingsQueryDtos,
                                                              List<AttendStatusCountQueryDto> attendStatusCountQueryDtos) {

        List<TodayMeetingResponseDto> todayMeetingResponseDtos = todayMeetingsQueryDtos.stream()
                .map(TodayMeetingResponseDto::of)
                .collect(Collectors.toList());
        attendStatusCountQueryDtos.forEach(attendStatusCountDto -> {
            todayMeetingResponseDtos.stream()
                    .filter(todayMeetingResponseDto -> todayMeetingResponseDto.getMeetingId().equals(attendStatusCountDto.getMeetingId()))
                    .findFirst()
                    .ifPresent(todayMeetingResponseDto -> todayMeetingResponseDto.setCount(attendStatusCountDto));
        });
        return todayMeetingResponseDtos;
    }

    private static TodayMeetingResponseDto of(TodayMeetingsQueryDto todayMeetingsQueryDto) {
        return TodayMeetingResponseDto.builder()
                .meetingId(todayMeetingsQueryDto.getMeetingId())
                .meetingName(todayMeetingsQueryDto.getMeetingName())
                .meetingDate(todayMeetingsQueryDto.getMeetingDate())
                .startTime(todayMeetingsQueryDto.getStartTime())
                .endTime(todayMeetingsQueryDto.getEndTime())
                .operationTeamId(todayMeetingsQueryDto.getOperationTeamId())
                .operationTeamName(todayMeetingsQueryDto.getOperationTeamName())
                .agendaContent(todayMeetingsQueryDto.getAgendaContent())
                .build();
    }

    private void setCount(AttendStatusCountQueryDto attendStatusCountQueryDto) {
        if (attendStatusCountQueryDto.getMeetingStatus().equals(AttendStatus.ATTEND.name())) {
            attends = attendStatusCountQueryDto.getCount();
        } else if (attendStatusCountQueryDto.getMeetingStatus().equals(AttendStatus.ABSENT.name())) {
            absents = attendStatusCountQueryDto.getCount();
        } else {
            unknowns = attendStatusCountQueryDto.getCount();
        }
    }
}
