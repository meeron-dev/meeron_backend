package com.cmc.meeron.attendee.application.port.in.response;

import java.util.List;

public class AttendeeResponseDtoBuilder {

    public static List<AttendeeResponseDto> buildListAdmins() {
        return List.of(
                AttendeeResponseDto.builder()
                        .attendeeId(1L)
                        .meetingId(1L)
                        .meetingAdmin(true)
                        .attendStatus("ATTEND")
                        .attendeeWorkspaceUserResponseDto(AttendeeWorkspaceUserResponseDto.builder()
                                .workspaceUserId(1L)
                                .workspaceId(1L)
                                .isWorkspaceAdmin(false)
                                .nickname("테스트1")
                                .position("프론트엔드")
                                .profileImageUrl("https://test.com/123")
                                .email("test1@test.com")
                                .phone("010-1234-1234")
                                .build())
                        .build(),
                AttendeeResponseDto.builder()
                        .attendeeId(2L)
                        .meetingId(1L)
                        .meetingAdmin(true)
                        .attendStatus("ATTEND")
                        .attendeeWorkspaceUserResponseDto(AttendeeWorkspaceUserResponseDto.builder()
                                .workspaceUserId(2L)
                                .workspaceId(1L)
                                .isWorkspaceAdmin(false)
                                .nickname("테스트2")
                                .position("프론트엔드")
                                .profileImageUrl("https://test.com/143")
                                .email("test2@test.com")
                                .phone("010-1234-1235")
                                .build())
                        .build()
        );
    }

    public static AttendeeResponseDto build() {
        return AttendeeResponseDto.builder()
                .attendeeId(1L)
                .meetingId(1L)
                .meetingAdmin(true)
                .attendStatus("ATTEND")
                .attendeeWorkspaceUserResponseDto(AttendeeWorkspaceUserResponseDto.builder()
                        .workspaceUserId(1L)
                        .workspaceId(1L)
                        .isWorkspaceAdmin(false)
                        .nickname("테스트1")
                        .position("프론트엔드")
                        .profileImageUrl("https://test.com/123")
                        .email("test1@test.com")
                        .phone("010-1234-1234")
                        .build())
                .build();
    }
}
