package com.cmc.meeron.attendee.application.port.in.response;

import com.cmc.meeron.attendee.application.port.out.response.MeetingAttendeesCountsByTeamQueryDto;
import com.cmc.meeron.attendee.domain.AttendStatus;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeetingAttendeesResponseDto {

    private Long teamId;
    private String teamName;
    private int attends;
    private int absents;
    private int unknowns;

    public static List<MeetingAttendeesResponseDto> fromQueryDtos(List<MeetingAttendeesCountsByTeamQueryDto> queryDtos) {
        List<MeetingAttendeesResponseDto> responseDtos = new ArrayList<>();
        queryDtos.stream()
                .collect(Collectors.groupingBy(MeetingAttendeesCountsByTeamQueryDto::getTeamId))
                .forEach((key, value) ->
                        responseDtos.add(MeetingAttendeesResponseDto.fromGroupByTeamIdList(value)));
        return responseDtos;
    }

    private static MeetingAttendeesResponseDto fromGroupByTeamIdList(List<MeetingAttendeesCountsByTeamQueryDto> groupByTeamIdDtos) {
        AtomicInteger attend = new AtomicInteger();
        AtomicInteger absent = new AtomicInteger();
        AtomicInteger unknowns = new AtomicInteger();
        groupByTeamIdDtos.forEach(dto -> {
            if (dto.getAttendStatus().equals(AttendStatus.ATTEND.name())) {
                attend.set(dto.getCount());
            } else if (dto.getAttendStatus().equals(AttendStatus.ABSENT.name())) {
                absent.set(dto.getCount());
            } else {
                unknowns.set(dto.getCount());
            }
        });
        return MeetingAttendeesResponseDto.builder()
                .teamId(groupByTeamIdDtos.get(0).getTeamId())
                .teamName(groupByTeamIdDtos.get(0).getTeamName())
                .attends(attend.get())
                .absents(absent.get())
                .unknowns(unknowns.get())
                .build();
    }
}
