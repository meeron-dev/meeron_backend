package com.cmc.meeron.meeting.application.dto.request;

import lombok.*;

import java.time.Year;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonthMeetingsCountRequestDto {

    private MeetingSearchRequestDto meetingSearch;
    private Year year;

    public static MonthMeetingsCountRequestDto of(String type, List<Long> id, Year year) {
        return MonthMeetingsCountRequestDto.builder()
                .meetingSearch(MeetingSearchRequestDto.builder()
                        .searchType(type)
                        .searchIds(id)
                        .build())
                .year(year)
                .build();
    }

    public String getSearchType() {
        return meetingSearch.getSearchType();
    }

    public List<Long> getSearchIds() {
        return meetingSearch.getSearchIds();
    }
}
