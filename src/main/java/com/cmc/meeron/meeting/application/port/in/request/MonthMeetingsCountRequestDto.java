package com.cmc.meeron.meeting.application.port.in.request;

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

    public String getSearchType() {
        return meetingSearch.getSearchType();
    }

    public List<Long> getSearchIds() {
        return meetingSearch.getSearchIds();
    }
}
