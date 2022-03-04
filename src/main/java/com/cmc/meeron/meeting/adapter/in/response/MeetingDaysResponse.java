package com.cmc.meeron.meeting.adapter.in.response;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeetingDaysResponse {

    @Builder.Default
    private List<Integer> days = new ArrayList<>();

    public static MeetingDaysResponse of(List<Integer> days) {
        return MeetingDaysResponse.builder()
                .days(days)
                .build();
    }
}
