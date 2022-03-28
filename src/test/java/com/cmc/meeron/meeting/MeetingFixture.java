package com.cmc.meeron.meeting;

import com.cmc.meeron.meeting.domain.Attendee;
import com.cmc.meeron.meeting.domain.Attendees;
import com.cmc.meeron.meeting.domain.Meeting;

import java.util.ArrayList;
import java.util.Arrays;

import static com.cmc.meeron.team.TeamFixture.TEAM_1;
import static com.cmc.meeron.workspace.WorkspaceUserFixture.WORKSPACE_USER_1;
import static com.cmc.meeron.workspace.WorkspaceUserFixture.WORKSPACE_USER_2;
import static com.cmc.meeron.workspace.WorkspaceFixture.WORKSPACE_1;

public class MeetingFixture {

    public static final Meeting MEETING = Meeting.builder()
            .id(3L)
            .workspace(WORKSPACE_1)
            .team(TEAM_1)
            .build();

    public static final Meeting MEETING_ATTEND_ATTENDEES = Meeting.builder()
            .id(4L)
            .workspace(WORKSPACE_1)
            .team(TEAM_1)
            .attendees(Attendees.builder()
                    .values(new ArrayList<>(Arrays.asList(
                            Attendee.builder().workspaceUser(WORKSPACE_USER_1).build(),
                            Attendee.builder().workspaceUser(WORKSPACE_USER_2).build())))
                    .build())
            .build();
}
