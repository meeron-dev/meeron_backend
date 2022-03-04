package com.cmc.meeron.meeting.adapter.in.response;

import com.cmc.meeron.meeting.application.port.in.response.YearMeetingsCountResponseDto;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class YearMeetingsCountResponse {

    @Builder.Default
    private List<MeetingsYearCount> yearCounts = new ArrayList<>();

    public static YearMeetingsCountResponse of(List<YearMeetingsCountResponseDto> yearMeetingsCount) {
        return YearMeetingsCountResponse.builder()
                .yearCounts(yearMeetingsCount.stream()
                        .map(count -> MeetingsYearCount.builder()
                                .year(count.getYear())
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
    public static class MeetingsYearCount {
        private int year;
        private Long count;
    }
}
