package com.cmc.meeron.meeting.adapter.in.response;

import com.cmc.meeron.meeting.application.port.in.response.AgendaCountResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgendaCountResponse {

    private boolean active;
    private int checks;
    private int files;

    public static AgendaCountResponse fromResponseDto(AgendaCountResponseDto responseDto) {
        return AgendaCountResponse.builder()
                .active(responseDto.isActive())
                .checks(responseDto.getChecks())
                .files(responseDto.getFiles())
                .build();
    }
}
