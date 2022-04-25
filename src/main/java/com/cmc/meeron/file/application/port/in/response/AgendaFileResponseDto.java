package com.cmc.meeron.file.application.port.in.response;

import com.cmc.meeron.file.domain.AgendaFile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgendaFileResponseDto {

    private Long fileId;
    private String fileName;
    private String fileUrl;

    public static List<AgendaFileResponseDto> from(List<AgendaFile> agendaFiles) {
        return agendaFiles.stream()
                .map(AgendaFileResponseDto::from)
                .collect(Collectors.toList());
    }

    private static AgendaFileResponseDto from(AgendaFile agendaFile) {
        return AgendaFileResponseDto.builder()
                .fileId(agendaFile.getId())
                .fileName(agendaFile.getOriginFileName())
                .fileUrl(agendaFile.getUrl())
                .build();
    }
}
