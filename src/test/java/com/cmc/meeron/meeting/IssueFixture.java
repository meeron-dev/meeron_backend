package com.cmc.meeron.meeting;

import com.cmc.meeron.meeting.domain.Issue;

import static com.cmc.meeron.meeting.AgendaFixture.AGENDA1;

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
