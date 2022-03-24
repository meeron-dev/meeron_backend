package com.cmc.meeron.meeting;

import com.cmc.meeron.meeting.domain.Attendee;

import static com.cmc.meeron.meeting.MeetingFixture.MEETING;
import static com.cmc.meeron.user.WorkspaceUserFixture.WORKSPACE_USER_1;

public class AttendeeFixture {

    public static final Attendee ATTENDEE_1 = Attendee.builder()
            .id(1L)
            .workspaceUser(WORKSPACE_USER_1)
            .isMeetingAdmin(true)
            .meeting(MEETING)
            .build();
}
