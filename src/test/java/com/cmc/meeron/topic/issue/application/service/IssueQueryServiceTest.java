package com.cmc.meeron.topic.issue.application.service;

import com.cmc.meeron.topic.issue.application.port.in.response.IssueResponseDto;
import com.cmc.meeron.topic.issue.application.port.out.IssueQueryPort;
import com.cmc.meeron.topic.issue.domain.Issue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.cmc.meeron.topic.issue.IssueFixture.ISSUE_1;
import static com.cmc.meeron.topic.issue.IssueFixture.ISSUE_2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IssueQueryServiceTest {

    @Mock
    IssueQueryPort issueQueryPort;
    @InjectMocks
    IssueQueryService issueQueryService;

    @DisplayName("아젠다의 이슈들 조회 - 성공")
    @Test
    void get_agenda_issues_success() throws Exception {

        // given
        List<Issue> issues = List.of(ISSUE_1, ISSUE_2);
        when(issueQueryPort.findByAgendaId(any()))
                .thenReturn(issues);

        // when
        List<IssueResponseDto> responseDtos = issueQueryService.getAgendaIssues(1L);

        // then
        assertAll(
                () -> verify(issueQueryPort).findByAgendaId(1L),
                () -> assertThat(responseDtos)
                        .usingRecursiveComparison()
                        .isEqualTo(IssueResponseDto.from(issues))
        );
    }
}
