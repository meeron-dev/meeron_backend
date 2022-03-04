package com.cmc.meeron.meeting.application.port.out.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

@Getter
@NoArgsConstructor
@Builder
public class MonthMeetingsCountQueryDto {

    private int month;
    private Long count;

    @QueryProjection
    public MonthMeetingsCountQueryDto(int month, Long count) {
        this.month = month;
        this.count = count;
    }
}
