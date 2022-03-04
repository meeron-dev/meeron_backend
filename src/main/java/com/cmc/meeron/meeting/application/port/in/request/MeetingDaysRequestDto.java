package com.cmc.meeron.meeting.application.port.in.request;

import com.cmc.meeron.meeting.adapter.in.request.MeetingDaysRequest;
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

    public static MeetingDaysRequestDto of(MeetingDaysRequest meetingDaysRequest) {
        return MeetingDaysRequestDto.builder()
                .meetingSearch(MeetingSearchRequestDto.builder()
                        .searchType(meetingDaysRequest.getType())
                        .searchIds(meetingDaysRequest.getId())
                        .build())
                .yearMonth(meetingDaysRequest.getDate())
                .build();
    }

    public String getSearchType() {
        return meetingSearch.getSearchType();
    }

    public List<Long> getSearchIds() {
        return meetingSearch.getSearchIds();
    }
}
