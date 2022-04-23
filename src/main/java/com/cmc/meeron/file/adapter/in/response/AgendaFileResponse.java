package com.cmc.meeron.file.adapter.in.response;

import com.cmc.meeron.file.application.port.in.response.AgendaFileResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgendaFileResponse {

    private Long fileId;
    private String fileName;
    private String fileUrl;

    public static AgendaFileResponse from(AgendaFileResponseDto agendaFileResponseDto) {
        return AgendaFileResponse.builder()
                .fileId(agendaFileResponseDto.getFileId())
                .fileName(agendaFileResponseDto.getFileName())
                .fileUrl(agendaFileResponseDto.getFileUrl())
                .build();
    }
}
