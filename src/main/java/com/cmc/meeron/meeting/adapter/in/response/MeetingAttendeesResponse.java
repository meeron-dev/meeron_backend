package com.cmc.meeron.meeting.adapter.in.response;

import com.cmc.meeron.meeting.application.port.in.response.MeetingAttendeesResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeetingAttendeesResponse {

    @Builder.Default
    private List<AttendeeTeamCountResponse> attendees = new ArrayList<>();

    public static MeetingAttendeesResponse fromResponseDtos(List<MeetingAttendeesResponseDto> responseDtos) {
        return MeetingAttendeesResponse.builder()
                .attendees(responseDtos
                        .stream()
                        .map(AttendeeTeamCountResponse::fromResponseDto)
                        .collect(Collectors.toList()))
                .build();
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AttendeeTeamCountResponse {

        private Long teamId;
        private String teamName;
        private int attends;
        private int absents;
        private int unknowns;

        public static AttendeeTeamCountResponse fromResponseDto(MeetingAttendeesResponseDto meetingAttendeesResponseDto) {
            return AttendeeTeamCountResponse.builder()
                    .teamId(meetingAttendeesResponseDto.getTeamId())
                    .teamName(meetingAttendeesResponseDto.getTeamName())
                    .attends(meetingAttendeesResponseDto.getAttends())
                    .absents(meetingAttendeesResponseDto.getAbsents())
                    .unknowns(meetingAttendeesResponseDto.getUnknowns())
                    .build();
        }
    }
}
