package com.cmc.meeron.meeting.domain;

import com.cmc.meeron.common.exception.meeting.AttendeeDuplicateException;
import com.cmc.meeron.user.domain.WorkspaceUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.cmc.meeron.meeting.MeetingFixture.MEETING_ATTEND_ATTENDEES;
import static com.cmc.meeron.user.WorkspaceUserFixture.WORKSPACE_USER_1;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AttendeesTest {

    private Meeting meeting;
    private List<WorkspaceUser> duplicateWorkspaceUsers;

    @BeforeEach
    void setUp() {
        meeting = MEETING_ATTEND_ATTENDEES;
        duplicateWorkspaceUsers = List.of(WORKSPACE_USER_1);
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
}
