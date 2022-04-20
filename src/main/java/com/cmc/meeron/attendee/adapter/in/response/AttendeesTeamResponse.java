package com.cmc.meeron.attendee.adapter.in.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendeesTeamResponse {

    private Long teamId;
    private String teamName;
}
