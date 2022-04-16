package com.cmc.meeron.topic.issue;

import com.cmc.meeron.topic.issue.domain.Issue;

import static com.cmc.meeron.topic.agenda.AgendaFixture.AGENDA1;

public class IssueFixture {

    public static final Issue ISSUE_1 = Issue.builder()
            .id(1L)
            .agenda(AGENDA1)
            .contents("테스트이슈1")
            .build();

    public static final Issue ISSUE_2 = Issue.builder()
            .id(2L)
            .agenda(AGENDA1)
            .contents("테스트이슈2")
            .build();
}
