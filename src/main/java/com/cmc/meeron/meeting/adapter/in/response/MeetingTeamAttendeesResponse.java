package com.cmc.meeron.meeting.adapter.in.response;

import com.cmc.meeron.meeting.application.port.in.response.MeetingTeamAttendeesResponseDto;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeetingTeamAttendeesResponse {

    @Builder.Default
    private List<AttendeesResponse> attends = new ArrayList<>();

    @Builder.Default
    private List<AttendeesResponse> absents = new ArrayList<>();

    @Builder.Default
    private List<AttendeesResponse> unknowns = new ArrayList<>();

    public static MeetingTeamAttendeesResponse fromResponseDto(MeetingTeamAttendeesResponseDto responseDto) {
        return MeetingTeamAttendeesResponse.builder()
                .attends(responseDto.getAttends()
                        .stream()
                        .map(AttendeesResponse::fromResponseDto)
                        .collect(Collectors.toList()))
                .absents(responseDto.getAbsents()
                        .stream()
                        .map(AttendeesResponse::fromResponseDto)
                        .collect(Collectors.toList()))
                .unknowns(responseDto.getUnknowns()
                        .stream()
                        .map(AttendeesResponse::fromResponseDto)
                        .collect(Collectors.toList()))
                .build();
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AttendeesResponse {

        private Long workspaceUserId;
        private String profileImageUrl;
        private String nickname;
        private String position;

        public static AttendeesResponse fromResponseDto(MeetingTeamAttendeesResponseDto.AttendeesResponseDto attendeesResponseDto) {
            return AttendeesResponse.builder()
                    .workspaceUserId(attendeesResponseDto.getWorkspaceUserId())
                    .profileImageUrl(attendeesResponseDto.getProfileImageUrl())
                    .nickname(attendeesResponseDto.getNickname())
                    .position(attendeesResponseDto.getPosition())
                    .build();
        }
    }
}
