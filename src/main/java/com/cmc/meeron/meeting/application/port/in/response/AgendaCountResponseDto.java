package com.cmc.meeron.meeting.application.port.in.response;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgendaCountResponseDto {

    private boolean active;
    private int checks;
    private int files;

    public static AgendaCountResponseDto ofFalse() {
        return AgendaCountResponseDto.builder()
                .active(false)
                .checks(0)
                .files(0)
                .build();
    }

    public static AgendaCountResponseDto ofTrue(int fileCount) {
        return AgendaCountResponseDto.builder()
                .active(true)
                .checks(0)
                .files(fileCount)
                .build();
    }
}
