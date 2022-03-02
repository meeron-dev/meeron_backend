package com.cmc.meeron.meeting.application.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class YearMeetingsCountResponseDto {

    private int year;
    private Long count;
}
