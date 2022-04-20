package com.cmc.meeron.attendee.application.port.in.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendCountResponseDto {

    private int attend;
    private int absent;
    private int unknown;

    public static AttendCountResponseDto fromCount(int attend, int absent, int unknown) {
        return AttendCountResponseDto.builder()
                .attend(attend)
                .absent(absent)
                .unknown(unknown)
                .build();
    }
}
