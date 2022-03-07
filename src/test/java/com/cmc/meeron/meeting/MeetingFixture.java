package com.cmc.meeron.meeting;

import com.cmc.meeron.meeting.domain.Meeting;

import static com.cmc.meeron.team.TeamFixture.TEAM;
import static com.cmc.meeron.workspace.WorkspaceFixture.WORKSPACE_1;

public class MeetingFixture {

    public static final Meeting MEETING = Meeting.builder()
            .id(3L)
            .workspace(WORKSPACE_1)
            .team(TEAM)
            .build();
}
