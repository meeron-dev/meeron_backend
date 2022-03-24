package com.cmc.meeron.meeting.application.service;

import com.cmc.meeron.common.exception.meeting.AttendeeDuplicateException;
import com.cmc.meeron.common.exception.meeting.AttendeeNotFoundException;
import com.cmc.meeron.common.exception.meeting.MeetingNotFoundException;
import com.cmc.meeron.common.exception.meeting.NotWorkspacesTeamException;
import com.cmc.meeron.common.exception.team.TeamNotFoundException;
import com.cmc.meeron.common.exception.user.WorkspaceUserNotFoundException;
import com.cmc.meeron.common.exception.workspace.WorkspaceNotFoundException;
import com.cmc.meeron.common.exception.workspace.WorkspaceUsersNotInEqualWorkspaceException;
import com.cmc.meeron.common.security.AuthUser;
import com.cmc.meeron.meeting.application.port.in.request.*;
import com.cmc.meeron.meeting.application.port.out.MeetingCommandPort;
import com.cmc.meeron.meeting.application.port.out.MeetingQueryPort;
import com.cmc.meeron.meeting.domain.Agenda;
import com.cmc.meeron.meeting.domain.Meeting;
import com.cmc.meeron.team.application.port.out.TeamQueryPort;
import com.cmc.meeron.team.domain.Team;
import com.cmc.meeron.user.application.port.out.UserQueryPort;
import com.cmc.meeron.user.domain.WorkspaceUser;
import com.cmc.meeron.workspace.application.port.out.WorkspaceQueryPort;
import com.cmc.meeron.workspace.domain.Workspace;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.cmc.meeron.auth.AuthUserFixture.AUTH_USER;
import static com.cmc.meeron.meeting.AgendaFixture.AGENDA1;
import static com.cmc.meeron.meeting.MeetingFixture.MEETING;
import static com.cmc.meeron.meeting.MeetingFixture.MEETING_ATTEND_ATTENDEES;
import static com.cmc.meeron.user.WorkspaceUserFixture.*;
import static com.cmc.meeron.workspace.WorkspaceFixture.WORKSPACE_1;
import static com.cmc.meeron.workspace.WorkspaceFixture.WORKSPACE_2;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MeetingCommandServiceTest {

    @Mock TeamQueryPort teamQueryPort;
    @Mock WorkspaceQueryPort workspaceQueryPort;
    @Mock UserQueryPort userQueryPort;
    @Mock MeetingCommandPort meetingCommandPort;
    @Mock MeetingQueryPort meetingQueryPort;
    @InjectMocks MeetingCommandService meetingCommandService;

    private Meeting meeting;
    private Agenda agenda;
    private AuthUser authUser;
    private Workspace workspace;

    @BeforeEach
    void setUp() {
        meeting = MEETING;
        agenda = AGENDA1;
        authUser = AUTH_USER;
        workspace = createWorkspace();
    }

    private CreateMeetingRequestDto createCreateMeetingRequestDto() {
        LocalTime nowTime = LocalTime.now();
        return CreateMeetingRequestDto.builder()
                .workspaceId(workspace.getId())
                .meetingName("테스트회의")
                .meetingPurpose("테스트회의성격")
                .startDate(LocalDate.now())
                .startTime(nowTime)
                .endTime(nowTime.plusHours(1))
                .operationTeamId(1L)
                .meetingAdminIds(List.of(1L, 2L))
                .build();
    }

    private Workspace createWorkspace() {
        return Workspace.builder()
                .id(1L)
                .build();
    }

    private Team createTeam() {
        return Team.builder()
                .id(1L)
                .workspace(createWorkspace())
                .build();
    }

    @DisplayName("회의 생성 - 실패 / 주관하는 팀이 존재하지 않을 경우")
    @Test
    void create_meeting_fail_not_exist_team() throws Exception {

        // given
        CreateMeetingRequestDto createMeetingRequestDto = createCreateMeetingRequestDto();
        when(teamQueryPort.findById(createMeetingRequestDto.getOperationTeamId()))
                .thenThrow(TeamNotFoundException.class);

        // when, then
        assertThrows(TeamNotFoundException.class,
                () -> meetingCommandService.createMeeting(createMeetingRequestDto, authUser));
    }

    @DisplayName("회의 생성 - 실패 / 워크스페이스가 존재하지 않을 경우")
    @Test
    void create_meeting_fail_not_exist_workspace() throws Exception {

        // given
        CreateMeetingRequestDto createMeetingRequestDto = createCreateMeetingRequestDto();
        Team team = createTeam();
        when(teamQueryPort.findById(createMeetingRequestDto.getOperationTeamId()))
                .thenReturn(Optional.of(team));
        when(workspaceQueryPort.findById(createCreateMeetingRequestDto().getWorkspaceId()))
                .thenReturn(Optional.empty());

        // when, then
        assertThrows(WorkspaceNotFoundException.class,
                () -> meetingCommandService.createMeeting(createMeetingRequestDto, authUser));
    }

    @DisplayName("회의 생성 - 실패 / 내가 존재하지 않을 경우")
    @Test
    void create_meeting_fail_not_in_equal_workspace() throws Exception {

        // given
        CreateMeetingRequestDto createMeetingRequestDto = createCreateMeetingRequestDto();
        Team operationTeam = createTeam();
        when(teamQueryPort.findById(any()))
                .thenReturn(Optional.of(operationTeam));
        Workspace workspace = createWorkspace();
        when(workspaceQueryPort.findById(createMeetingRequestDto.getWorkspaceId()))
                .thenReturn(Optional.of(workspace));
        when(userQueryPort.findAllWorkspaceUsersByIds(createMeetingRequestDto.getMeetingAdminIds()))
                .thenReturn(Collections.emptyList());
        when(userQueryPort.findByUserWorkspaceId(authUser.getUserId(), createMeetingRequestDto.getWorkspaceId()))
                .thenReturn(Optional.empty());

        // when, then
        assertThrows(WorkspaceUserNotFoundException.class,
                () -> meetingCommandService.createMeeting(createMeetingRequestDto, authUser));
    }

    @DisplayName("회의 생성 - 실패 / 찾은 워크스페이스 유저들이 워크스페이스에 속하지 않은 경우")
    @Test
    void create_meeting_fail_team_not_workspace_users_in_equal_workspace() throws Exception {

        // given
        CreateMeetingRequestDto createMeetingRequestDto = createCreateMeetingRequestDto();
        Team team = createTeam();
        when(teamQueryPort.findById(createMeetingRequestDto.getOperationTeamId()))
                .thenReturn(Optional.of(team));
        Workspace workspace = createWorkspace();
        when(workspaceQueryPort.findById(createMeetingRequestDto.getWorkspaceId()))
                .thenReturn(Optional.of(workspace));
        when(userQueryPort.findAllWorkspaceUsersByIds(any()))
                .thenReturn(List.of(WorkspaceUser.builder()
                        .workspace(WORKSPACE_2)
                        .id(1728L).build()));
        when(userQueryPort.findByUserWorkspaceId(authUser.getUserId(), createMeetingRequestDto.getWorkspaceId()))
                .thenReturn(Optional.of(WorkspaceUser.builder()
                        .id(1234L)
                        .workspace(WORKSPACE_1)
                        .user(authUser.getUser())
                        .build()));

        // when, then
        assertThrows(WorkspaceUsersNotInEqualWorkspaceException.class,
                () -> meetingCommandService.createMeeting(createMeetingRequestDto, authUser));
    }

    @DisplayName("회의 생성 - 실패 / 워크스페이스에 속한 팀이 아닌 경우")
    @Test
    void create_meeting_fail_team_not_in_workspace() throws Exception {

        // given
        CreateMeetingRequestDto createMeetingRequestDto = createCreateMeetingRequestDto();
        Team team = Team.builder()
                .id(2L)
                .workspace(WORKSPACE_2)
                .build();
        when(teamQueryPort.findById(createMeetingRequestDto.getOperationTeamId()))
                .thenReturn(Optional.of(team));
        Workspace workspace = createWorkspace();
        when(workspaceQueryPort.findById(createMeetingRequestDto.getWorkspaceId()))
                .thenReturn(Optional.of(workspace));
        when(userQueryPort.findAllWorkspaceUsersByIds(any()))
                .thenReturn(List.of(WorkspaceUser.builder()
                        .workspace(workspace)
                        .id(1728L).build()));
        when(userQueryPort.findByUserWorkspaceId(authUser.getUserId(), createMeetingRequestDto.getWorkspaceId()))
                .thenReturn(Optional.of(WorkspaceUser.builder()
                        .id(1234L)
                        .workspace(workspace)
                        .user(authUser.getUser())
                        .build()));

        // when, then
        assertThrows(NotWorkspacesTeamException.class,
                () -> meetingCommandService.createMeeting(createMeetingRequestDto, authUser));
    }

    @DisplayName("회의 생성 - 성공")
    @Test
    void create_meeting_success() throws Exception {

        // given
        CreateMeetingRequestDto createMeetingRequestDto = createCreateMeetingRequestDto();
        Workspace workspace = createWorkspace();
        Team team = createTeam(workspace);
        when(teamQueryPort.findById(createMeetingRequestDto.getOperationTeamId()))
                .thenReturn(Optional.of(team));
        when(workspaceQueryPort.findById(createMeetingRequestDto.getWorkspaceId()))
                .thenReturn(Optional.of(workspace));
        List<WorkspaceUser> workspaceUsers = createWorkspaceUsers(workspace);
        when(userQueryPort.findAllWorkspaceUsersByIds(createMeetingRequestDto.getMeetingAdminIds()))
                .thenReturn(workspaceUsers);
        when(userQueryPort.findByUserWorkspaceId(authUser.getUserId(), createMeetingRequestDto.getWorkspaceId()))
                .thenReturn(Optional.of(WorkspaceUser.builder()
                        .id(1234L)
                        .workspace(workspace)
                        .user(authUser.getUser())
                        .build()));
        when(meetingCommandPort.saveMeeting(any(Meeting.class)))
                .thenReturn(1L);
        when(userQueryPort.findByUserWorkspaceId(authUser.getUserId(), createMeetingRequestDto.getWorkspaceId()))
                .thenReturn(Optional.of(WorkspaceUser.builder()
                        .id(1234L)
                        .workspace(workspace)
                        .user(authUser.getUser())
                        .build()));

        // when
        Long response = meetingCommandService.createMeeting(createMeetingRequestDto, authUser);

        // then
        assertAll(
                () -> verify(teamQueryPort).findById(createMeetingRequestDto.getOperationTeamId()),
                () -> verify(workspaceQueryPort).findById(createMeetingRequestDto.getWorkspaceId()),
                () -> verify(userQueryPort).findAllWorkspaceUsersByIds(createMeetingRequestDto.getMeetingAdminIds()),
                () -> verify(userQueryPort).findByUserWorkspaceId(authUser.getUserId(), workspace.getId()),
                () -> verify(meetingCommandPort).saveMeeting(any(Meeting.class))
        );
    }

    private Team createTeam(Workspace workspace) {
        return Team.builder()
                .id(1L)
                .workspace(workspace)
                .build();
    }

    private List<WorkspaceUser> createWorkspaceUsers(Workspace workspace) {
        return List.of(
                WorkspaceUser.builder().id(1L).workspace(workspace).build(),
                WorkspaceUser.builder().id(2L).workspace(workspace).build()
        );
    }

    @DisplayName("아젠다 생성 - 실패 / 회의가 존재하지 않을 경우")
    @Test
    void create_agenda_fail_not_found_meeting() throws Exception {

        // given
        CreateAgendaRequestDto requestDto = createCreateAgendaRequestDto();
        when(meetingQueryPort.findById(any()))
                .thenThrow(new MeetingNotFoundException());

        // when, then
        assertThrows(
                MeetingNotFoundException.class,
                () -> meetingCommandService.createAgendas(requestDto)
        );
    }

    private CreateAgendaRequestDto createCreateAgendaRequestDto() {
        return CreateAgendaRequestDto.builder()
                .meetingId(1L)
                .agendaRequestDtos(List.of(
                        CreateAgendaRequestDto.AgendaRequestDto.builder()
                                .agendaName("테스트아젠다1")
                                .agendaOrder(1)
                                .issues(List.of("테스트이슈1", "테스트이슈2"))
                                .build(),
                        CreateAgendaRequestDto.AgendaRequestDto.builder()
                                .agendaName("테스트아젠다2")
                                .agendaOrder(2)
                                .issues(List.of("테스트이슈1", "테스트이슈2"))
                                .build()))
                .build();
    }

    @DisplayName("아젠다 생성 - 성공")
    @Test
    void create_agenda_success() throws Exception {

        // given
        CreateAgendaRequestDto requestDto = createCreateAgendaRequestDto();
        when(meetingQueryPort.findById(any()))
                .thenReturn(Optional.of(meeting));
        when(meetingCommandPort.saveAgenda(any(Agenda.class)))
                .thenReturn(agenda);

        // when
        List<Long> responseDtos = meetingCommandService.createAgendas(requestDto);

        // then
        assertAll(
                () -> verify(meetingCommandPort, times(requestDto.getAgendaRequestDtos().size()))
                        .saveAgenda(any(Agenda.class)),
                () -> verify(meetingCommandPort, times(2))
                        .saveIssues(anyList()),
                () -> assertEquals(responseDtos.size(), requestDto.getAgendaRequestDtos().size())
        );
    }

    @DisplayName("회의 참가자 추가 - 성공")
    @Test
    void join_attendees_success() throws Exception {

        // given
        JoinAttendeesRequestDto requestDto = createJoinAttendeesRequestDto();
        when(meetingQueryPort.findWithAttendeesById(any()))
                .thenReturn(Optional.of(meeting));
        when(workspaceQueryPort.findById(any()))
                .thenReturn(Optional.of(WORKSPACE_1));
        when(userQueryPort.findAllWorkspaceUsersByIds(any()))
                .thenReturn(List.of(WORKSPACE_USER_3, WORKSPACE_USER_4));

        // when
        meetingCommandService.joinAttendees(requestDto);

        // then
        assertAll(
                () -> verify(meetingQueryPort).findWithAttendeesById(requestDto.getMeetingId()),
                () -> verify(workspaceQueryPort).findById(meeting.getWorkspace().getId()),
                () -> verify(userQueryPort).findAllWorkspaceUsersByIds(requestDto.getWorkspaceUserIds()),
                () -> assertEquals(requestDto.getWorkspaceUserIds().size(), meeting.getAttendees().size())
        );
    }

    private JoinAttendeesRequestDto createJoinAttendeesRequestDto() {
        return JoinAttendeesRequestDto.builder()
                .meetingId(1L)
                .workspaceUserIds(List.of(1L, 2L))
                .build();
    }

    @DisplayName("회의 참가자 추가 - 실패 / 회의가 존재하지 않을 경우")
    @Test
    void join_attendees_fail_not_found_meeting() throws Exception {

        // given
        JoinAttendeesRequestDto requestDto = createJoinAttendeesRequestDto();
        when(meetingQueryPort.findWithAttendeesById(any()))
                .thenReturn(Optional.empty());

        // when, then
        assertThrows(
                MeetingNotFoundException.class,
                () -> meetingCommandService.joinAttendees(requestDto)
        );
    }

    @DisplayName("회의 참가자 추가 - 실패 / 워크스페이스가 존재하지 않을 경우")
    @Test
    void join_attendees_fail_not_found_workspace() throws Exception {

        // given
        JoinAttendeesRequestDto requestDto = createJoinAttendeesRequestDto();
        when(meetingQueryPort.findWithAttendeesById(any()))
                .thenReturn(Optional.of(meeting));
        when(workspaceQueryPort.findById(any()))
                .thenReturn(Optional.empty());

        // when, then
        assertThrows(
                WorkspaceNotFoundException.class,
                () -> meetingCommandService.joinAttendees(requestDto)
        );
    }

    @DisplayName("회의 참가자 추가 - 실패 / 이미 참여한 참가자인 경우")
    @Test
    void join_attendees_fail_not_duplicate_attendees() throws Exception {

        // given
        JoinAttendeesRequestDto requestDto = createJoinAttendeesRequestDto();
        when(meetingQueryPort.findWithAttendeesById(any()))
                .thenReturn(Optional.of(MEETING_ATTEND_ATTENDEES));
        when(workspaceQueryPort.findById(any()))
                .thenReturn(Optional.of(meeting.getWorkspace()));
        when(userQueryPort.findAllWorkspaceUsersByIds(any()))
                .thenReturn(List.of(WORKSPACE_USER_1));

        // when, then
        assertThrows(AttendeeDuplicateException.class,
                () -> meetingCommandService.joinAttendees(requestDto));
    }

    @DisplayName("회의 참가자 추가 - 실패 / 회의 참가자 워크스페이스가 회의 워크스페이스와 다른 경우")
    @Test
    void join_attendees_fail_attendees_in_another_workspace() throws Exception {

        // given
        when(meetingQueryPort.findWithAttendeesById(any()))
                .thenReturn(Optional.of(meeting));
        when(workspaceQueryPort.findById(any()))
                .thenReturn(Optional.of(meeting.getWorkspace()));
        when(userQueryPort.findAllWorkspaceUsersByIds(any()))
                .thenReturn(List.of(WORKSPACE_USER_2));
        JoinAttendeesRequestDto requestDto = createJoinAttendeesRequestDto();

        // when, then
        assertThrows(
                WorkspaceUsersNotInEqualWorkspaceException.class,
                () -> meetingCommandService.joinAttendees(requestDto)
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
                () -> meetingCommandService.changeAttendStatus(requestDto));
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
                () -> meetingCommandService.changeAttendStatus(requestDto));
    }

    @DisplayName("회의 참가자 상태 변경 - 성공")
    @Test
    void change_attend_status_success() throws Exception {

        // given
        ChangeAttendStatusRequestDto requestDto = ChangeAttendStatusRequestDtoBuilder.build();
        when(meetingQueryPort.findWithAttendeesById(any()))
                .thenReturn(Optional.of(MEETING_ATTEND_ATTENDEES));

        // when
        meetingCommandService.changeAttendStatus(requestDto);

        // then
        verify(meetingQueryPort).findWithAttendeesById(requestDto.getMeetingId());
    }
}
