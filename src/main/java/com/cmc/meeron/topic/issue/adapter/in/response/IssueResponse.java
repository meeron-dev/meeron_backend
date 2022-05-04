package com.cmc.meeron.topic.issue.adapter.in.response;

import com.cmc.meeron.topic.issue.application.port.in.response.IssueResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IssueResponse {

    private Long issueId;
    private String content;
    private String issueResult;

    public static IssueResponse from(IssueResponseDto issueResponseDto) {
        return IssueResponse.builder()
                .issueId(issueResponseDto.getIssueId())
                .content(issueResponseDto.getContent())
                .issueResult(issueResponseDto.getIssueResult())
                .build();
    }
}
