package com.cmc.meeron.attendee.application.port.out.response;

import java.util.List;

public class MeetingAttendeesCountsByTeamQueryDtoBuilder {

    public static List<MeetingAttendeesCountsByTeamQueryDto> buildList() {
        return List.of(
                MeetingAttendeesCountsByTeamQueryDto.builder()
                        .teamId(1L)
                        .teamName("1팀")
                        .attendStatus("ATTEND")
                        .count(1)
                        .build(),
                MeetingAttendeesCountsByTeamQueryDto.builder()
                        .teamId(1L)
                        .teamName("1팀")
                        .attendStatus("UNKNOWN")
                        .count(2)
                        .build(),
                MeetingAttendeesCountsByTeamQueryDto.builder()
                        .teamId(2L)
                        .teamName("2팀")
                        .attendStatus("UNKNOWN")
                        .count(3)
                        .build(),
                MeetingAttendeesCountsByTeamQueryDto.builder()
                        .teamId(2L)
                        .teamName("2팀")
                        .attendStatus("ABSENT")
                        .count(1)
                        .build(),
                MeetingAttendeesCountsByTeamQueryDto.builder()
                        .teamId(3L)
                        .teamName("3팀")
                        .attendStatus("ATTEND")
                        .count(3)
                        .build()
        );
    }
}
