package com.cmc.meeron.meeting.application.port.out.response;

import com.cmc.meeron.meeting.application.port.out.response.AttendStatusCountQueryDto;

import java.util.List;

public class AttendStatusCountResponseDtoBuilder {

    public static List<AttendStatusCountQueryDto> buildList() {
        return List.of(
                AttendStatusCountQueryDto.builder()
                        .meetingId(1L)
                        .meetingStatus("ATTEND")
                        .count(3)
                        .build(),
                AttendStatusCountQueryDto.builder()
                        .meetingId(2L)
                        .meetingStatus("ATTEND")
                        .count(1)
                        .build(),
                AttendStatusCountQueryDto.builder()
                        .meetingId(1L)
                        .meetingStatus("UNKNOWN")
                        .count(2)
                        .build(),
                AttendStatusCountQueryDto.builder()
                        .meetingId(2L)
                        .meetingStatus("UNKNOWN")
                        .count(1)
                        .build(),
                AttendStatusCountQueryDto.builder()
                        .meetingId(1L)
                        .meetingStatus("ABSENT")
                        .count(5)
                        .build()
        );
    }
}
