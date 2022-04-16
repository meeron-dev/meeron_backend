package com.cmc.meeron.topic.agenda.application.port.in.response;

import com.cmc.meeron.file.domain.AgendaFile;
import com.cmc.meeron.topic.agenda.domain.Agenda;
import com.cmc.meeron.topic.issue.domain.Issue;
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
public class AgendaIssuesFilesResponseDto {

    private Long agendaId;
    private String agendaName;
    @Builder.Default
    private List<IssueResponseDto> issues = new ArrayList<>();
    @Builder.Default
    private List<FileResponseDto> files = new ArrayList<>();

    public static AgendaIssuesFilesResponseDto fromEntities(Agenda agenda, List<Issue> issues, List<AgendaFile> files) {
        return AgendaIssuesFilesResponseDto.builder()
                .agendaId(agenda.getId())
                .agendaName(agenda.getName())
                .issues(issues.stream()
                        .map(IssueResponseDto::fromEntity)
                        .collect(Collectors.toList()))
                .files(files.stream()
                        .map(FileResponseDto::fromEntity)
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

        public static IssueResponseDto fromEntity(Issue issue) {
            return IssueResponseDto.builder()
                    .issueId(issue.getId())
                    .content(issue.getContents())
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

        public static FileResponseDto fromEntity(AgendaFile file) {
            return FileResponseDto.builder()
                    .fileId(file.getId())
                    .fileName(file.getOriginFileName())
                    .fileUrl(file.getUrl())
                    .build();
        }
    }
}
