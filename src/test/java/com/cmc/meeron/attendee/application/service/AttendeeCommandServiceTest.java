package com.cmc.meeron.attendee.application.service;

import com.cmc.meeron.attendee.application.port.in.request.JoinAttendeesRequestDtoBuilder;
import com.cmc.meeron.attendee.application.port.in.request.ChangeAttendStatusRequestDto;
import com.cmc.meeron.attendee.application.port.in.request.JoinAttendeesRequestDto;
import com.cmc.meeron.attendee.domain.Attendee;
import com.cmc.meeron.common.exception.meeting.AttendeeDuplicateException;
import com.cmc.meeron.common.exception.meeting.AttendeeNotFoundException;
import com.cmc.meeron.common.exception.meeting.MeetingNotFoundException;
import com.cmc.meeron.common.exception.workspace.WorkspaceUsersNotInEqualWorkspaceException;
import com.cmc.meeron.meeting.application.port.in.request.ChangeAttendStatusRequestDtoBuilder;
import com.cmc.meeron.meeting.application.port.out.MeetingQueryPort;
import com.cmc.meeron.meeting.domain.Attendees;
import com.cmc.meeron.meeting.domain.Meeting;
import com.cmc.meeron.workspace.domain.WorkspaceUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.cmc.meeron.meeting.MeetingFixture.MEETING_ATTEND_ATTENDEES;
import static com.cmc.meeron.workspace.WorkspaceFixture.WORKSPACE_1;
import static com.cmc.meeron.workspace.WorkspaceUserFixture.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AttendeeCommandServiceTest {

    @Mock
    MeetingQueryPort meetingQueryPort;
    @Mock
    JoinMeetingValidator joinMeetingValidator;
    @InjectMocks
    AttendeeCommandService attendeeCommandService;

    private Meeting meetingWithAttendee;

    @BeforeEach
    void setUp() {
        meetingWithAttendee = Meeting.builder()
                .workspace(WORKSPACE_1)
                .attendees(Attendees.builder()
                        .values(new ArrayList<>(Arrays.asList(Attendee.builder()
                                .workspaceUser(WorkspaceUser.builder()
                                        .workspace(WORKSPACE_1)
                                        .build())
                                .build())))
                        .build())
                .build();
    }

    @AfterEach
    void tearDown() {
        meetingWithAttendee = null;
    }

    @DisplayName("회의 참가자 추가 - 성공")
    @Test
    void join_attendees_success() throws Exception {

        // given
        JoinAttendeesRequestDto requestDto = JoinAttendeesRequestDtoBuilder.build();
        when(meetingQueryPort.findWithAttendeesById(any()))
                .thenReturn(Optional.of(meetingWithAttendee));
        when(joinMeetingValidator.workspaceUsersInEqualWorkspace(any(), any()))
                .thenReturn(List.of(WORKSPACE_USER_3, WORKSPACE_USER_4));

        // when
        attendeeCommandService.joinAttendees(requestDto);

        // then
        assertAll(
                () -> verify(meetingQueryPort).findWithAttendeesById(requestDto.getMeetingId()),
                () -> verify(joinMeetingValidator).workspaceUsersInEqualWorkspace(requestDto.getWorkspaceUserIds(),
                        meetingWithAttendee.getWorkspace()),
                () -> assertEquals(requestDto.getWorkspaceUserIds().size() + 1, meetingWithAttendee.getAttendees().size())
        );
    }

    @DisplayName("회의 참가자 추가 - 실패 / 회의가 존재하지 않을 경우")
    @Test
    void join_attendees_fail_not_found_meeting() throws Exception {

        // given
        JoinAttendeesRequestDto requestDto = JoinAttendeesRequestDtoBuilder.build();
        when(meetingQueryPort.findWithAttendeesById(any()))
                .thenReturn(Optional.empty());

        // when, then
        assertThrows(
                MeetingNotFoundException.class,
                () -> attendeeCommandService.joinAttendees(requestDto)
        );
    }

    @DisplayName("회의 참가자 추가 - 실패 / 이미 참여한 참가자인 경우")
    @Test
    void join_attendees_fail_not_duplicate_attendees() throws Exception {

        // given
        JoinAttendeesRequestDto requestDto = JoinAttendeesRequestDtoBuilder.build();
        when(meetingQueryPort.findWithAttendeesById(any()))
                .thenReturn(Optional.of(meetingWithAttendee));
        when(joinMeetingValidator.workspaceUsersInEqualWorkspace(any(), any()))
                .thenReturn(meetingWithAttendee.getAttendees()
                        .getValues()
                        .stream()
                        .map(Attendee::getWorkspaceUser)
                        .collect(Collectors.toList()));

        // when, then
        assertThrows(AttendeeDuplicateException.class,
                () -> attendeeCommandService.joinAttendees(requestDto));
    }

    @DisplayName("회의 참가자 추가 - 실패 / 회의 참가자 워크스페이스가 회의 워크스페이스와 다른 경우")
    @Test
    void join_attendees_fail_attendees_in_another_workspace() throws Exception {

        // given
        JoinAttendeesRequestDto requestDto = JoinAttendeesRequestDtoBuilder.build();
        when(meetingQueryPort.findWithAttendeesById(any()))
                .thenReturn(Optional.of(meetingWithAttendee));
        when(joinMeetingValidator.workspaceUsersInEqualWorkspace(any(), any()))
                .thenThrow(new WorkspaceUsersNotInEqualWorkspaceException());

        // when, then
        assertThrows(
                WorkspaceUsersNotInEqualWorkspaceException.class,
                () -> attendeeCommandService.joinAttendees(requestDto)
        );
    }

    @DisplayName("회의 참가자 상태 변경 - 실패 / 회의를 찾을 수 없을 경우")
    @Test
    void change_attend_status_fail_not_found_meeting() throws Exception {

        // given
        ChangeAttendStatusRequestDto requestDto = ChangeAttendStatusRequestDtoBuilder.build();
        when(meetingQueryPort.findWithAttendeesById(any()))
                .thenReturn(Optional.empty());

        // when, then
        assertThrows(MeetingNotFoundException.class,
                () -> attendeeCommandService.changeAttendStatus(requestDto));
    }

    @DisplayName("회의 참가자 상태 변경 - 실패 / 존재하지 않는 참가자인 경우")
    @Test
    void change_attend_status_fail_not_attendee() throws Exception {

        // given
        ChangeAttendStatusRequestDto requestDto = ChangeAttendStatusRequestDtoBuilder.buildFailRequest();
        when(meetingQueryPort.findWithAttendeesById(any()))
                .thenReturn(Optional.of(MEETING_ATTEND_ATTENDEES));

        // when, then
        assertThrows(AttendeeNotFoundException.class,
                () -> attendeeCommandService.changeAttendStatus(requestDto));
    }

    @DisplayName("회의 참가자 상태 변경 - 성공")
    @Test
    void change_attend_status_success() throws Exception {

        // given
        ChangeAttendStatusRequestDto requestDto = ChangeAttendStatusRequestDtoBuilder.build();
        when(meetingQueryPort.findWithAttendeesById(any()))
                .thenReturn(Optional.of(MEETING_ATTEND_ATTENDEES));

        // when
        attendeeCommandService.changeAttendStatus(requestDto);

        // then
        verify(meetingQueryPort).findWithAttendeesById(requestDto.getMeetingId());
    }
}
