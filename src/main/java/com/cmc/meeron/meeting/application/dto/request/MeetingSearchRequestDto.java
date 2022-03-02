package com.cmc.meeron.meeting.application.dto.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeetingSearchRequestDto {

    private String searchType;
    private List<Long> searchIds;
}
