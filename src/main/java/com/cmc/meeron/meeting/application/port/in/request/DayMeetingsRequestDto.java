package com.cmc.meeron.meeting.application.port.in.request;

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

    public String getSearchType() {
        return meetingSearch.getSearchType();
    }

    public List<Long> getSearchIds() {
        return meetingSearch.getSearchIds();
    }
}
