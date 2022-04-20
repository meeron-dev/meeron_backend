package com.cmc.meeron.attendee.adapter.in.response;

import com.cmc.meeron.attendee.application.port.in.response.MeetingAttendeesCountsByTeamResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeetingAttendeesCountsByTeamResponse {

    private List<AttendeesCountsByTeamResponse> attendeesCountByTeam;

    public static MeetingAttendeesCountsByTeamResponse fromResponseDtos(List<MeetingAttendeesCountsByTeamResponseDto> responseDtos) {
        return MeetingAttendeesCountsByTeamResponse.builder()
                .attendeesCountByTeam(responseDtos
                        .stream()
                        .map(responseDto -> AttendeesCountsByTeamResponse.builder()
                                .team(AttendeesTeamResponse.builder()
                                        .teamId(responseDto.getAttendTeamResponseDto().getTeamId())
                                        .teamName(responseDto.getAttendTeamResponseDto().getTeamName())
                                        .build())
                                .count(AttendeesStatusCountResponse.builder()
                                        .attend(responseDto.getAttendCountResponseDto().getAttend())
                                        .absent(responseDto.getAttendCountResponseDto().getAbsent())
                                        .unknown(responseDto.getAttendCountResponseDto().getUnknown())
                                        .build())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AttendeesCountsByTeamResponse {

        private AttendeesTeamResponse team;
        private AttendeesStatusCountResponse count;
    }
}
