package com.cmc.meeron.meeting.application.port.out.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
public class FirstAgendaQueryDto {

    private Long meetingId;
    private Long agendaId;
    private String agendaContents;

    @QueryProjection
    public FirstAgendaQueryDto(Long meetingId, Long agendaId, String agendaContents) {
        this.meetingId = meetingId;
        this.agendaId = agendaId;
        this.agendaContents = agendaContents;
    }
}
