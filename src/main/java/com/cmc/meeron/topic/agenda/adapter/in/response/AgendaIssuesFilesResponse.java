package com.cmc.meeron.topic.agenda.adapter.in.response;

import com.cmc.meeron.topic.agenda.application.port.in.response.AgendaIssuesFilesResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgendaIssuesFilesResponse {

    private Long agendaId;
    private String agendaName;
    @Builder.Default
    private List<IssueResponseDto> issues = new ArrayList<>();
    @Builder.Default
    private List<FileResponseDto> files = new ArrayList<>();

    public static AgendaIssuesFilesResponse fromResponseDto(AgendaIssuesFilesResponseDto agendaIssuesFilesResponseDto) {
        return AgendaIssuesFilesResponse.builder()
                .agendaId(agendaIssuesFilesResponseDto.getAgendaId())
                .agendaName(agendaIssuesFilesResponseDto.getAgendaName())
                .issues(agendaIssuesFilesResponseDto
                        .getIssues()
                        .stream()
                        .map(IssueResponseDto::fromResponseDto)
                        .collect(Collectors.toList()))
                .files(agendaIssuesFilesResponseDto
                        .getFiles()
                        .stream()
                        .map(FileResponseDto::fromResponseDto)
                        .collect(Collectors.toList()))
                .build();
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class IssueResponseDto {

        private Long issueId;
        private String content;

        private static IssueResponseDto fromResponseDto(AgendaIssuesFilesResponseDto.IssueResponseDto issue) {
            return IssueResponseDto.builder()
                    .issueId(issue.getIssueId())
                    .content(issue.getContent())
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FileResponseDto {

        private Long fileId;
        private String fileName;
        private String fileUrl;

        private static FileResponseDto fromResponseDto(AgendaIssuesFilesResponseDto.FileResponseDto file) {
            return FileResponseDto.builder()
                    .fileId(file.getFileId())
                    .fileName(file.getFileName())
                    .fileUrl(file.getFileUrl())
                    .build();
        }
    }
}
