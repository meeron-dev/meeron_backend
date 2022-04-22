package com.cmc.meeron.attendee.application.port.in.response;

import com.cmc.meeron.attendee.application.port.out.response.MeetingAttendeesCountsByTeamQueryDto;
import com.cmc.meeron.attendee.domain.AttendStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeetingAttendeesCountsByTeamResponseDto {

    private AttendTeamResponseDto attendTeamResponseDto;
    private AttendCountResponseDto attendCountResponseDto;

    public static List<MeetingAttendeesCountsByTeamResponseDto> fromQueryDtos(List<MeetingAttendeesCountsByTeamQueryDto> meetingAttendeesCountsByTeamQueryDtos) {
        List<MeetingAttendeesCountsByTeamResponseDto> responseDtos = new ArrayList<>();
        meetingAttendeesCountsByTeamQueryDtos.stream()
                .collect(Collectors.groupingBy(MeetingAttendeesCountsByTeamQueryDto::getTeamId))
                .forEach((key, value) ->
                        responseDtos.add(MeetingAttendeesCountsByTeamResponseDto.fromGroupByTeamId(value)));
        return responseDtos;
    }

    private static MeetingAttendeesCountsByTeamResponseDto fromGroupByTeamId(List<MeetingAttendeesCountsByTeamQueryDto> values) {
        AtomicInteger attend = new AtomicInteger();
        AtomicInteger absent = new AtomicInteger();
        AtomicInteger unknown = new AtomicInteger();
        values.forEach(dto -> {
            if (dto.getAttendStatus().equals(AttendStatus.ATTEND.name())) {
                attend.set(dto.getCount());
            } else if (dto.getAttendStatus().equals(AttendStatus.ABSENT.name())) {
                absent.set(dto.getCount());
            } else {
                unknown.set(dto.getCount());
            }
        });
        return MeetingAttendeesCountsByTeamResponseDto.builder()
                .attendTeamResponseDto(AttendTeamResponseDto.fromTeamIdName(values.get(0).getTeamId(), values.get(0).getTeamName()))
                .attendCountResponseDto(AttendCountResponseDto.fromCount(attend.get(), absent.get(), unknown.get()))
                .build();
    }
}
