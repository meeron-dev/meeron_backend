package com.cmc.meeron.meeting.application.port.in.response;

import java.util.List;

public class MeetingAttendeesResponseDtoBuilder {

    public static MeetingAttendeesResponseDto build() {
        return MeetingAttendeesResponseDto.builder()
                .attends(List.of(
                        MeetingAttendeesResponseDto.AttendeesResponseDto.builder()
                                .workspaceUserId(1L)
                                .profileImageUrl("")
                                .nickname("참가자1")
                                .position("백엔드")
                                .build(),
                        MeetingAttendeesResponseDto.AttendeesResponseDto.builder()
                                .workspaceUserId(2L)
                                .profileImageUrl("")
                                .nickname("참가자2")
                                .position("백엔드")
                                .build()))
                .absents(List.of(
                        MeetingAttendeesResponseDto.AttendeesResponseDto.builder()
                                .workspaceUserId(3L)
                                .profileImageUrl("")
                                .nickname("참가자3")
                                .position("프론트엔드")
                                .build(),
                        MeetingAttendeesResponseDto.AttendeesResponseDto.builder()
                                .workspaceUserId(4L)
                                .profileImageUrl("")
                                .nickname("참가자4")
                                .position("안드로이드")
                                .build()))
                .unknowns(List.of(
                        MeetingAttendeesResponseDto.AttendeesResponseDto.builder()
                                .workspaceUserId(5L)
                                .profileImageUrl("https://image.com/123123")
                                .nickname("참가자5")
                                .position("iOS")
                                .build()))
                .build();
    }
}
