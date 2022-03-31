package com.cmc.meeron.meeting.application.port.out.response;

import java.util.List;

public class MeetingAttendeesQueryDtoBuilder {

    public static List<MeetingAttendeesQueryDto> buildList() {
        return List.of(
                MeetingAttendeesQueryDto.builder()
                        .teamId(1L)
                        .teamName("1팀")
                        .attendStatus("ATTEND")
                        .count(1)
                        .build(),
                MeetingAttendeesQueryDto.builder()
                        .teamId(1L)
                        .teamName("1팀")
                        .attendStatus("UNKNOWN")
                        .count(2)
                        .build(),
                MeetingAttendeesQueryDto.builder()
                        .teamId(2L)
                        .teamName("2팀")
                        .attendStatus("UNKNOWN")
                        .count(3)
                        .build(),
                MeetingAttendeesQueryDto.builder()
                        .teamId(2L)
                        .teamName("2팀")
                        .attendStatus("ABSENT")
                        .count(1)
                        .build(),
                MeetingAttendeesQueryDto.builder()
                        .teamId(3L)
                        .teamName("3팀")
                        .attendStatus("ATTEND")
                        .count(3)
                        .build()
        );
    }
}
