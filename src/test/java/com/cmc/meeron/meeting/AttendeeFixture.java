package com.cmc.meeron.meeting;

import com.cmc.meeron.meeting.domain.AttendStatus;
import com.cmc.meeron.meeting.domain.Attendee;

import static com.cmc.meeron.meeting.MeetingFixture.MEETING;
import static com.cmc.meeron.workspace.WorkspaceUserFixture.WORKSPACE_USER_1;
import static com.cmc.meeron.workspace.WorkspaceUserFixture.WORKSPACE_USER_2;

public class AttendeeFixture {

    public static final Attendee ATTENDEE_1 = Attendee.builder()
            .id(1L)
            .workspaceUser(WORKSPACE_USER_1)
            .isMeetingAdmin(true)
            .meeting(MEETING)
            .attendStatus(AttendStatus.ATTEND)
            .build();

    public static final Attendee ATTENDEE_2 = Attendee.builder()
            .id(2L)
            .workspaceUser(WORKSPACE_USER_2)
            .isMeetingAdmin(true)
            .attendStatus(null)
            .meeting(MEETING)
            .attendStatus(AttendStatus.UNKNOWN)
            .build();
}
