package com.cmc.meeron.meeting;

import com.cmc.meeron.attendee.domain.Attendee;
import com.cmc.meeron.meeting.domain.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;

import static com.cmc.meeron.team.TeamFixture.TEAM_1;
import static com.cmc.meeron.workspaceuser.WorkspaceUserFixture.WORKSPACE_USER_1;
import static com.cmc.meeron.workspaceuser.WorkspaceUserFixture.WORKSPACE_USER_2;
import static com.cmc.meeron.workspace.WorkspaceFixture.WORKSPACE_1;

public class MeetingFixture {

    public static final Meeting MEETING = Meeting.builder()
            .id(3L)
            .workspace(WORKSPACE_1)
            .team(TEAM_1)
            .meetingTime(MeetingTime.builder()
                    .startDate(LocalDate.now())
                    .startTime(LocalTime.now())
                    .endTime(LocalTime.now().plusHours(2))
                    .build())
            .meetingInfo(MeetingInfo.builder()
                    .purpose("테스트성격1")
                    .name("테스트회의명1")
                    .build())
            .place("테스트장소1")
            .deleted(false)
            .build();

    public static final Meeting MEETING_ATTEND_ATTENDEES = Meeting.builder()
            .id(4L)
            .workspace(WORKSPACE_1)
            .team(TEAM_1)
            .meetingTime(MeetingTime.builder()
                    .startDate(LocalDate.now())
                    .startTime(LocalTime.now())
                    .endTime(LocalTime.now().plusHours(2))
                    .build())
            .meetingInfo(MeetingInfo.builder()
                    .purpose("테스트성격2")
                    .name("테스트회의명2")
                    .build())
            .place("테스트장소1")
            .deleted(false)
            .attendees(Attendees.builder()
                    .values(new ArrayList<>(Arrays.asList(
                            Attendee.builder().workspaceUser(WORKSPACE_USER_1).build(),
                            Attendee.builder().workspaceUser(WORKSPACE_USER_2).build())))
                    .build())
            .build();
}
