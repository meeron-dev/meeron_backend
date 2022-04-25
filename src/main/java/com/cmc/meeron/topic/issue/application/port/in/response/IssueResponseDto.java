package com.cmc.meeron.topic.issue.application.port.in.response;

import com.cmc.meeron.topic.issue.domain.Issue;
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
public class IssueResponseDto {

    private Long issueId;
    private String content;
    private String issueResult;

    public static List<IssueResponseDto> from(List<Issue> issues) {
        return issues.stream()
                .map(IssueResponseDto::from)
                .collect(Collectors.toList());
    }

    private static IssueResponseDto from(Issue issue) {
        return IssueResponseDto.builder()
                .issueId(issue.getId())
                .content(issue.getContents())
                .issueResult(issue.getIssueResult())
                .build();
    }
}
