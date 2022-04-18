package com.cmc.meeron.meeting.domain;

import com.cmc.meeron.attendee.domain.AttendStatus;
import com.cmc.meeron.attendee.domain.Attendee;
import com.cmc.meeron.common.exception.meeting.AttendeeDuplicateException;
import com.cmc.meeron.common.exception.meeting.AttendeeNotFoundException;
import com.cmc.meeron.common.exception.meeting.NotMeetingAdminException;
import com.cmc.meeron.workspaceuser.domain.WorkspaceUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static com.cmc.meeron.attendee.AttendeeFixture.ADMIN_ATTENDEE;
import static com.cmc.meeron.attendee.AttendeeFixture.NOT_ADMIN_ATTENDEE;
import static com.cmc.meeron.meeting.MeetingFixture.MEETING_ATTEND_ATTENDEES;
import static com.cmc.meeron.workspaceuser.WorkspaceUserFixture.WORKSPACE_USER_1;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AttendeesTest {

    private Meeting meeting;
    private List<WorkspaceUser> duplicateWorkspaceUsers;
    private Attendee attendee;
    private Attendee notAdmin;

    @BeforeEach
    void setUp() {
        meeting = MEETING_ATTEND_ATTENDEES;
        duplicateWorkspaceUsers = List.of(WORKSPACE_USER_1);
        attendee = ADMIN_ATTENDEE;
        notAdmin = NOT_ADMIN_ATTENDEE;
    }

    @DisplayName("회의 참가자 생성 - 실패 / 이미 참여중인 회원이 있는 경우")
    @Test
    void add_attendees_fail_duplicate_attendees() throws Exception {

        // when, then
        assertThrows(
                AttendeeDuplicateException.class,
                () -> meeting.addAttendees(duplicateWorkspaceUsers)
        );
    }

    @DisplayName("회의 참가자 생성 - 성공")
    @Test
    void add_attendees_success() throws Exception {

        // given
        WorkspaceUser newWorkspaceUser = WorkspaceUser.builder().id(1895L).build();

        // when
        meeting.addAttendees(List.of(newWorkspaceUser));

        // then
        assertEquals(3, meeting.getAttendees().size());
    }

    @DisplayName("회의 참가자 상태 변경 - 성공")
    @ParameterizedTest
    @MethodSource("changeAttendStatusArguments")
    void change_attend_status(AttendStatus attendStatus) throws Exception {

        // given
        Attendee attendee = Attendee.builder()
                .attendStatus(AttendStatus.UNKNOWN)
                .build();

        // when
        attendee.changeStatus(attendStatus);

        // then
        assertEquals(attendStatus, attendee.getAttendStatus());
    }

    private static Stream<Arguments> changeAttendStatusArguments() {
        return Stream.of(
                Arguments.of(AttendStatus.ATTEND),
                Arguments.of(AttendStatus.ACCIDENT),
                Arguments.of(AttendStatus.ABSENT)
        );
    }

    @DisplayName("워크스페이스 유저 ID로 참가자 찾기 - 실패 / 존재하지 않을 경우")
    @Test
    void find_by_workspace_user_id_fail_not_found_attendee() throws Exception {

        // given
        Attendees attendees = meeting.getAttendees();

        // when, then
        assertThrows(AttendeeNotFoundException.class,
                () -> attendees.findByWorkspaceUserId(12734128L));
    }

    @DisplayName("워크스페이스 유저 ID로 참가자 찾기 - 성공")
    @Test
    void find_by_workspace_user_id_success() throws Exception {

        // given
        Attendees attendees = meeting.getAttendees();

        // when
        Attendee attendee = attendees.findByWorkspaceUserId(59L);

        // then
        assertEquals(59L, attendee.getWorkspaceUser().getId());
    }

    @DisplayName("회의 관리자가 아닐 경우 예외 발생 - 성공")
    @Test
    void is_admin_or_throw_success() throws Exception {

        // when, then
        assertThrows(NotMeetingAdminException.class,
                () -> notAdmin.isAdminOrThrow());
    }
}
