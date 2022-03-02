package com.cmc.meeron.meeting.application.dto.request;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DayMeetingsRequestDto {

    private MeetingSearchRequestDto meetingSearch;
    private LocalDate localDate;

    public static DayMeetingsRequestDto of(String type, List<Long> ids, LocalDate date) {
        return DayMeetingsRequestDto.builder()
                .meetingSearch(MeetingSearchRequestDto.builder()
                        .searchType(type)
                        .searchIds(ids)
                        .build())
                .localDate(date)
                .build();
    }

    public String getSearchType() {
        return meetingSearch.getSearchType();
    }

    public List<Long> getSearchIds() {
        return meetingSearch.getSearchIds();
    }
}
