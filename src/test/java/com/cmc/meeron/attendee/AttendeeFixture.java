package com.cmc.meeron.attendee;

import com.cmc.meeron.attendee.domain.AttendStatus;
import com.cmc.meeron.attendee.domain.Attendee;

import static com.cmc.meeron.meeting.MeetingFixture.MEETING;
import static com.cmc.meeron.workspaceuser.WorkspaceUserFixture.WORKSPACE_USER_1;
import static com.cmc.meeron.workspaceuser.WorkspaceUserFixture.WORKSPACE_USER_2;

public class AttendeeFixture {

    public static final Attendee ADMIN_ATTENDEE = Attendee.builder()
            .id(1L)
            .workspaceUser(WORKSPACE_USER_1)
            .isMeetingAdmin(true)
            .meeting(MEETING)
            .attendStatus(AttendStatus.ATTEND)
            .build();

    public static final Attendee NOT_ADMIN_ATTENDEE = Attendee.builder()
            .id(2L)
            .workspaceUser(WORKSPACE_USER_2)
            .isMeetingAdmin(false)
            .attendStatus(null)
            .meeting(MEETING)
            .attendStatus(AttendStatus.UNKNOWN)
            .build();
}
