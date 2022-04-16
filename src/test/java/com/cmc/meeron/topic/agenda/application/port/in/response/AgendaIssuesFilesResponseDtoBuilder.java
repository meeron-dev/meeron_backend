package com.cmc.meeron.topic.agenda.application.port.in.response;

import com.cmc.meeron.topic.agenda.application.port.in.response.AgendaIssuesFilesResponseDto;

import java.util.List;

public class AgendaIssuesFilesResponseDtoBuilder {

    public static AgendaIssuesFilesResponseDto build() {
        return AgendaIssuesFilesResponseDto.builder()
                .agendaId(1L)
                .agendaName("테스트아젠다")
                .issues(List.of(
                        AgendaIssuesFilesResponseDto.IssueResponseDto.builder()
                                .issueId(1L)
                                .content("테스트이슈1")
                                .build(),
                        AgendaIssuesFilesResponseDto.IssueResponseDto.builder()
                                .issueId(2L)
                                .content("테스트이슈2")
                                .build()))
                .files(List.of(
                        AgendaIssuesFilesResponseDto.FileResponseDto.builder()
                                .fileId(1L)
                                .fileName("테스트파일명1")
                                .fileUrl("https://test.com/123123")
                                .build(),
                        AgendaIssuesFilesResponseDto.FileResponseDto.builder()
                                .fileId(2L)
                                .fileName("테스트파일명2")
                                .fileUrl("https://test.com/123143")
                                .build()))
                .build();
    }
}
