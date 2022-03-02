package com.cmc.meeron.meeting.domain.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

@Getter
@NoArgsConstructor
@Builder
public class MonthMeetingsCountDto {

    private int month;
    private Long count;

    @QueryProjection
    public MonthMeetingsCountDto(int month, Long count) {
        this.month = month;
        this.count = count;
    }
}
