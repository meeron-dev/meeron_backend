package com.cmc.meeron.meeting.application.port.in.request;

import lombok.*;

import java.time.YearMonth;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeetingDaysRequestDto {

    private MeetingSearchRequestDto meetingSearch;
    private YearMonth yearMonth;

    public String getSearchType() {
        return meetingSearch.getSearchType();
    }

    public List<Long> getSearchIds() {
        return meetingSearch.getSearchIds();
    }
}
