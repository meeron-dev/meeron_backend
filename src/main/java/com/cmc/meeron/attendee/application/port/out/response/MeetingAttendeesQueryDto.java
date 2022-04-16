package com.cmc.meeron.attendee.application.port.out.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
public class MeetingAttendeesQueryDto {

    private Long teamId;
    private String teamName;
    private String attendStatus;
    private int count;

    @QueryProjection
    public MeetingAttendeesQueryDto(Long teamId, String teamName, String attendStatus, int count) {
        this.teamId = teamId;
        this.teamName = teamName;
        this.attendStatus = attendStatus;
        this.count = count;
    }
}
