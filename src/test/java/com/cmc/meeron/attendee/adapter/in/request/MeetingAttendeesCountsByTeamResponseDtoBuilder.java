package com.cmc.meeron.attendee.adapter.in.request;

import com.cmc.meeron.attendee.application.port.in.response.AttendCountResponseDto;
import com.cmc.meeron.attendee.application.port.in.response.AttendTeamResponseDto;
import com.cmc.meeron.attendee.application.port.in.response.MeetingAttendeesCountsByTeamResponseDto;

import java.util.List;

public class MeetingAttendeesCountsByTeamResponseDtoBuilder {

    public static List<MeetingAttendeesCountsByTeamResponseDto> buildList() {
        return List.of(
                MeetingAttendeesCountsByTeamResponseDto.builder()
                        .attendCountResponseDto(
                                AttendCountResponseDto.builder()
                                        .absent(1)
                                        .unknown(2)
                                        .attend(3)
                                        .build()
                        )
                        .attendTeamResponseDto(
                                AttendTeamResponseDto.builder()
                                        .teamId(1L)
                                        .teamName("테스트팀1")
                                        .build()
                        )
                        .build(),
                MeetingAttendeesCountsByTeamResponseDto.builder()
                        .attendCountResponseDto(
                                AttendCountResponseDto.builder()
                                        .absent(3)
                                        .unknown(2)
                                        .attend(1)
                                        .build()
                        )
                        .attendTeamResponseDto(
                                AttendTeamResponseDto.builder()
                                        .teamId(2L)
                                        .teamName("테스트팀2")
                                        .build()
                        )
                        .build());
    }
}
