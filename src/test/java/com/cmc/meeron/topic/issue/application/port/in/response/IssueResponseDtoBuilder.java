package com.cmc.meeron.topic.issue.application.port.in.response;

import java.util.List;

public class IssueResponseDtoBuilder {

    public static List<IssueResponseDto> buildList() {
        return List.of(
                IssueResponseDto.builder()
                        .issueId(1L)
                        .content("테스트이슈1")
                        .issueResult("테스트이슈결과1")
                        .build(),
                IssueResponseDto.builder()
                        .issueId(2L)
                        .content("테스트이슈2")
                        .issueResult("테스트이슈결과2")
                        .build()
        );
    }
}
