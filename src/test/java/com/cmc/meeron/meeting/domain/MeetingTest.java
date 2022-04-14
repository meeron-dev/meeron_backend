package com.cmc.meeron.meeting.domain;

import com.cmc.meeron.attendee.domain.Attendee;
import com.cmc.meeron.team.domain.Team;
import com.cmc.meeron.workspace.domain.WorkspaceUser;
import com.cmc.meeron.workspace.domain.Workspace;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MeetingTest {

    private Meeting meetingEmptyAttendee;

    @BeforeEach
    void setUp() {
        LocalDate localDateNow = LocalDate.now();
        LocalTime localTimeNow = LocalTime.now();
        meetingEmptyAttendee = Meeting.builder()
                .id(1L)
                .workspace(Workspace.builder().id(2L).build())
                .team(Team.builder().id(3L).build())
                .meetingTime(MeetingTime.builder()
                        .startDate(localDateNow)
                        .startTime(localTimeNow)
                        .endTime(localTimeNow.plusHours(2))
                        .build())
                .meetingInfo(MeetingInfo.builder()
                        .name("테스트 회의1")
                        .purpose("테스트 목적")
                        .build())
                .place("회의실")
                .build();
    }

    @DisplayName("회의 생성 - 성공")
    @Test
    void create_meeting_success() throws Exception {

        // given
        Team team = Team.builder().id(2L).build();
        Workspace workspace = Workspace.builder().id(1L).build();
        LocalTime time = LocalTime.now();
        MeetingTime meetingTime = MeetingTime.builder()
                .startDate(LocalDate.now())
                .startTime(time)
                .endTime(time.plusHours(2))
                .build();
        MeetingInfo meetingInfo = MeetingInfo.builder()
                .name("테스트 회의명")
                .purpose("회의 성격")
                .build();

        // when
        Meeting meeting = Meeting.create(team, workspace, meetingTime, meetingInfo);

        // then
        assertAll(
                () -> assertEquals(team.getId(), meeting.getTeam().getId()),
                () -> assertEquals(workspace.getId(), meeting.getWorkspace().getId()),
                () -> assertEquals(meetingTime.getStartDate(), meeting.getMeetingTime().getStartDate()),
                () -> assertEquals(meetingTime.getStartTime(), meeting.getMeetingTime().getStartTime()),
                () -> assertEquals(meetingTime.getEndTime(), meeting.getMeetingTime().getEndTime()),
                () -> assertEquals(meetingInfo.getName(), meeting.getMeetingInfo().getName()),
                () -> assertEquals(meetingInfo.getPurpose(), meeting.getMeetingInfo().getPurpose())
        );
    }

    @DisplayName("회의 관리자 추가 - 성공")
    @Test
    void add_admin_success() throws Exception {

        // given
        List<WorkspaceUser> meetingAdmins = List.of(
                WorkspaceUser.builder().id(1L).build(),
                WorkspaceUser.builder().id(2L).build());

        // when
        meetingEmptyAttendee.addAdmins(meetingAdmins);

        // then
        List<Attendee> attendees = meetingEmptyAttendee.getAttendees().getValues();
        assertAll(
                () -> assertEquals(meetingAdmins.size(), attendees.size()),
                () -> assertEquals(meetingAdmins.size(), (int) attendees.stream()
                        .filter(Attendee::isMeetingAdmin).count())
        );
    }
}
