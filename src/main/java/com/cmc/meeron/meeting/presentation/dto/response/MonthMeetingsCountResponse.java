package com.cmc.meeron.meeting.presentation.dto.response;

import com.cmc.meeron.meeting.application.dto.response.MonthMeetingsCountResponseDto;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonthMeetingsCountResponse {

    @Builder.Default
    private List<MeetingsMonthCount> monthCounts = new ArrayList<>();

    public static MonthMeetingsCountResponse of(List<MonthMeetingsCountResponseDto> monthMeetingsCount) {
        return MonthMeetingsCountResponse.builder()
                .monthCounts(monthMeetingsCount.stream()
                        .map(count -> MeetingsMonthCount.builder()
                                .month(count.getMonth())
                                .count(count.getCount())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MeetingsMonthCount {

        private int month;
        private Long count;
    }
}
