package com.cmc.meeron.attendee.application.port.in.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendTeamResponseDto {

    private Long teamId;
    private String teamName;


    public static AttendTeamResponseDto fromTeamIdName(Long teamId, String teamName) {
        return AttendTeamResponseDto.builder()
                .teamId(teamId)
                .teamName(teamName)
                .build();
    }
}
