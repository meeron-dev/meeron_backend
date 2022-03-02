package com.cmc.meeron.meeting.domain.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
public class YearMeetingsCountDto {

    private int year;
    private Long count;

    @QueryProjection
    public YearMeetingsCountDto(int year, Long count) {
        this.year = year;
        this.count = count;
    }
}
