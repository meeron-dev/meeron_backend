package com.cmc.meeron.meeting.application.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonthMeetingsCountResponseDto implements Comparable<MonthMeetingsCountResponseDto> {

    private int month;
    private Long count;

    @Override
    public int compareTo(MonthMeetingsCountResponseDto o) {
        return this.getMonth() - o.getMonth();
    }
}
