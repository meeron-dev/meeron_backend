package com.cmc.meeron.topic.issue.adapter.in.response;

import com.cmc.meeron.topic.issue.application.port.in.response.IssueResponseDto;
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
public class IssueResponses {

    @Builder.Default
    private List<IssueResponse> issues = new ArrayList<>();

    public static IssueResponses from(List<IssueResponseDto> responseDtos) {
        return IssueResponses.builder()
                .issues(responseDtos.stream()
                        .map(IssueResponse::from)
                        .collect(Collectors.toList()))
                .build();
    }
}
