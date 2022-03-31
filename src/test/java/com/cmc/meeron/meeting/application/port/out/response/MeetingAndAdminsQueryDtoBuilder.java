package com.cmc.meeron.meeting.application.port.out.response;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class MeetingAndAdminsQueryDtoBuilder {

    public static MeetingAndAdminsQueryDto build() {
        LocalTime now = LocalTime.now();
        return MeetingAndAdminsQueryDto.builder()
                .meetingQueryDto(MeetingQueryDto.builder()
                        .meetingId(1L)
                        .meetingName("테스트회의")
                        .meetingPurpose("테스트목적")
                        .meetingDate(LocalDate.now())
                        .startTime(now)
                        .endTime(now.plusHours(2))
                        .operationTeamId(1L)
                        .operationTeamName("테스트팀")
                        .build())
                .adminQueryDtos(List.of(
                        AdminQueryDto.builder()
                                .workspaceUserId(1L)
                                .nickname("테스트1")
                                .build(),
                        AdminQueryDto.builder()
                                .workspaceUserId(2L)
                                .nickname("테스트2")
                                .build()
                ))
                .build();
    }
}
