package com.cmc.meeron.meeting.application.port.out;

import com.cmc.meeron.meeting.application.port.out.response.AttendStatusCountResponseDto;

import java.util.List;

public class AttendStatusCountResponseDtoBuilder {

    public static List<AttendStatusCountResponseDto> buildList() {
        return List.of(
                AttendStatusCountResponseDto.builder()
                        .meetingId(1L)
                        .meetingStatus("ATTEND")
                        .count(3)
                        .build(),
                AttendStatusCountResponseDto.builder()
                        .meetingId(2L)
                        .meetingStatus("ATTEND")
                        .count(1)
                        .build(),
                AttendStatusCountResponseDto.builder()
                        .meetingId(1L)
                        .meetingStatus("UNKNOWN")
                        .count(2)
                        .build(),
                AttendStatusCountResponseDto.builder()
                        .meetingId(2L)
                        .meetingStatus("UNKNOWN")
                        .count(1)
                        .build(),
                AttendStatusCountResponseDto.builder()
                        .meetingId(1L)
                        .meetingStatus("ABSENT")
                        .count(5)
                        .build()
        );
    }
}
