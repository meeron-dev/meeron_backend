package com.cmc.meeron.meeting.application.port.out.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
public class YearMeetingsCountQueryDto {

    private int year;
    private Long count;

    @QueryProjection
    public YearMeetingsCountQueryDto(int year, Long count) {
        this.year = year;
        this.count = count;
    }
}
