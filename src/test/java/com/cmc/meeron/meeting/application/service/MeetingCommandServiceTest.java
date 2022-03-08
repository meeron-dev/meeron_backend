package com.cmc.meeron.meeting.application.service;

import com.cmc.meeron.common.exception.meeting.MeetingNotFoundException;
import com.cmc.meeron.common.exception.meeting.NotWorkspacesTeamException;
import com.cmc.meeron.common.exception.team.TeamNotFoundException;
import com.cmc.meeron.common.exception.workspace.WorkspaceUsersNotInEqualWorkspaceException;
import com.cmc.meeron.meeting.application.port.in.request.CreateAgendaRequestDto;
import com.cmc.meeron.meeting.application.port.in.request.CreateMeetingRequestDto;
import com.cmc.meeron.meeting.application.port.in.request.JoinAttendeesRequestDto;
import com.cmc.meeron.meeting.application.port.in.response.CreateAgendaResponseDto;
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
import java.util.List;
import java.util.Optional;

import static com.cmc.meeron.meeting.AgendaFixture.AGENDA1;
import static com.cmc.meeron.meeting.MeetingFixture.MEETING;
import static com.cmc.meeron.team.TeamFixture.TEAM;
import static com.cmc.meeron.user.WorkspaceUserFixture.WORKSPACE_USER_1;
import static com.cmc.meeron.user.WorkspaceUserFixture.WORKSPACE_USER_2;
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

    private Team team;
    private Workspace workspace;
    private Meeting meeting;
    private Agenda agenda;

    @BeforeEach
    void setUp() {
        team = TEAM;
        workspace = WORKSPACE_1;
        meeting = MEETING;
        agenda = AGENDA1;
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
                () -> meetingCommandService.createMeeting(createMeetingRequestDto));
    }

    private CreateMeetingRequestDto createCreateMeetingRequestDto() {
        LocalTime nowTime = LocalTime.now();
        return CreateMeetingRequestDto.builder()
                .meetingName("테스트회의")
                .meetingPurpose("테스트회의성격")
                .startDate(LocalDate.now())
                .startTime(nowTime)
                .endTime(nowTime.plusHours(1))
                .operationTeamId(1L)
                .meetingManagerIds(List.of(1L, 2L))
                .build();
    }

    @DisplayName("회의 생성 - 실패 / 공동 관리자가 워크스페이스 내에 존재하지 않을 경우")
    @Test
    void create_meeting_fail_not_in_equal_workspace() throws Exception {

        // given
        CreateMeetingRequestDto createMeetingRequestDto = createCreateMeetingRequestDto();
        Team operationTeam = createTeam();
        when(teamQueryPort.findById(any()))
                .thenReturn(Optional.of(operationTeam));
        when(workspaceQueryPort.findByWorkspaceUserIds(createMeetingRequestDto
                .getMeetingManagerIds()))
                .thenReturn(createInvalidWorkspaceList());

        // when, then
        assertThrows(WorkspaceUsersNotInEqualWorkspaceException.class,
                () -> meetingCommandService.createMeeting(createMeetingRequestDto));
    }

    private Team createTeam() {
        return Team.builder()
                .id(1L)
                .workspace(createWorkspace())
                .build();
    }

    private Workspace createWorkspace() {
        return Workspace.builder()
                .id(1L)
                .build();
    }

    private List<Workspace> createInvalidWorkspaceList() {
        return List.of(
                createWorkspace(),
                Workspace.builder()
                        .id(2L)
                        .build()
        );
    }

    @DisplayName("회의 생성 - 실패 / 워크스페이스에 속한 팀이 아닌 경우")
    @Test
    void create_meeting_fail_team_not_in_workspace() throws Exception {

        // given
        CreateMeetingRequestDto createMeetingRequestDto = createCreateMeetingRequestDto();
        Team team = createTeam();
        when(teamQueryPort.findById(createMeetingRequestDto.getOperationTeamId()))
                .thenReturn(Optional.of(team));
        when(workspaceQueryPort.findByWorkspaceUserIds(any()))
                .thenReturn(List.of(Workspace.builder().id(1728L).build()));

        // when, then
        assertThrows(NotWorkspacesTeamException.class,
                () -> meetingCommandService.createMeeting(createMeetingRequestDto));
    }

    @DisplayName("회의 생성 - 성공")
    @Test
    void create_meeting_success() throws Exception {

        // given
        CreateMeetingRequestDto createMeetingRequestDto = createCreateMeetingRequestDto();
        Team team = createTeam();
        when(teamQueryPort.findById(createMeetingRequestDto.getOperationTeamId()))
                .thenReturn(Optional.of(team));
        Workspace workspace = team.getWorkspace();
        when(workspaceQueryPort.findByWorkspaceUserIds(createMeetingRequestDto.getMeetingManagerIds()))
                .thenReturn(List.of(workspace));
        List<WorkspaceUser> workspaceUsers = createWorkspaceUsers();
        when(userQueryPort.findAllWorkspaceUsersById(createMeetingRequestDto.getMeetingManagerIds()))
                .thenReturn(workspaceUsers);
        when(meetingCommandPort.saveMeeting(any(Meeting.class)))
                .thenReturn(1L);

        // when
        Long response = meetingCommandService.createMeeting(createMeetingRequestDto);

        // then
        assertAll(
                () -> verify(teamQueryPort).findById(createMeetingRequestDto.getOperationTeamId()),
                () -> verify(workspaceQueryPort).findByWorkspaceUserIds(createMeetingRequestDto.getMeetingManagerIds()),
                () -> verify(userQueryPort).findAllWorkspaceUsersById(createMeetingRequestDto.getMeetingManagerIds()),
                () -> verify(meetingCommandPort).saveMeeting(any(Meeting.class))
        );
    }

    private List<WorkspaceUser> createWorkspaceUsers() {
        return List.of(
                WorkspaceUser.builder().id(1L).workspace(createWorkspace()).build(),
                WorkspaceUser.builder().id(2L).workspace(createWorkspace()).build()
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
        List<CreateAgendaResponseDto> responseDtos = meetingCommandService.createAgendas(requestDto);

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
        when(meetingQueryPort.findById(any()))
                .thenReturn(Optional.of(meeting));
        when(workspaceQueryPort.findByWorkspaceUserIds(any()))
                .thenReturn(List.of(WORKSPACE_1));
        when(userQueryPort.findAllWorkspaceUsersById(any()))
                .thenReturn(List.of(WORKSPACE_USER_1, WORKSPACE_USER_2));

        // when
        meetingCommandService.joinAttendees(requestDto);

        // then
        assertAll(
                () -> verify(meetingQueryPort).findById(requestDto.getMeetingId()),
                () -> verify(workspaceQueryPort).findByWorkspaceUserIds(requestDto.getWorkspaceUserIds()),
                () -> verify(userQueryPort).findAllWorkspaceUsersById(requestDto.getWorkspaceUserIds()),
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
        when(meetingQueryPort.findById(any()))
                .thenReturn(Optional.empty());

        // when, then
        assertThrows(
                MeetingNotFoundException.class,
                () -> meetingCommandService.joinAttendees(requestDto)
        );
    }

    @DisplayName("회의 참가자 추가 - 실패 / 회의 참가자가 속한 워크스페이스가 회의의 워크스페이스와 다른 경우")
    @Test
    void join_attendees_fail_attendees_in_another_workspace() throws Exception {

        // given
        when(meetingQueryPort.findById(any()))
                .thenReturn(Optional.of(meeting));
        when(workspaceQueryPort.findByWorkspaceUserIds(any()))
                .thenReturn(List.of(WORKSPACE_1, WORKSPACE_2));
        JoinAttendeesRequestDto requestDto = createJoinAttendeesRequestDto();

        // when, then
        assertThrows(
                WorkspaceUsersNotInEqualWorkspaceException.class,
                () -> meetingCommandService.joinAttendees(requestDto)
        );
    }

    @DisplayName("회의 참가자 추가 - 실패 / 회의 참가자가 회의의 워크스페이스에 속해있지 않은 경우")
    @Test
    void join_attendees_fail_attendees_not_in_meeting_workspace() throws Exception {

        // given
        when(meetingQueryPort.findById(any()))
                .thenReturn(Optional.of(meeting));
        when(workspaceQueryPort.findByWorkspaceUserIds(any()))
                .thenReturn(List.of(WORKSPACE_2));
        JoinAttendeesRequestDto requestDto = createJoinAttendeesRequestDto();

        // when, then
        assertThrows(
                NotWorkspacesTeamException.class,
                () -> meetingCommandService.joinAttendees(requestDto)
        );
    }
}
