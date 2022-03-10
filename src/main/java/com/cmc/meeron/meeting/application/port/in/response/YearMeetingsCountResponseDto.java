package com.cmc.meeron.meeting.application.port.in.response;

import com.cmc.meeron.meeting.application.port.out.response.YearMeetingsCountQueryDto;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class YearMeetingsCountResponseDto {

    private int year;
    private Long count;

    public static List<YearMeetingsCountResponseDto> fromQueryResponseDtos(List<YearMeetingsCountQueryDto> yearMeetingsCounts) {
        return yearMeetingsCounts.stream()
                .map(YearMeetingsCountResponseDto::of)
                .collect(Collectors.toList());
    }

    private static YearMeetingsCountResponseDto of(YearMeetingsCountQueryDto yearMeetingsCountQueryDto) {
        return YearMeetingsCountResponseDto.builder()
                .year(yearMeetingsCountQueryDto.getYear())
                .count(yearMeetingsCountQueryDto.getCount())
                .build();
    }
}
