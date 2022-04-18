package com.cmc.meeron.meeting.application.service;

import com.cmc.meeron.common.exception.meeting.NotWorkspacesTeamException;
import com.cmc.meeron.common.exception.team.TeamNotFoundException;
import com.cmc.meeron.common.exception.workspace.WorkspaceNotFoundException;
import com.cmc.meeron.common.exception.workspace.WorkspaceUserNotFoundException;
import com.cmc.meeron.common.exception.workspace.WorkspaceUsersNotInEqualWorkspaceException;
import com.cmc.meeron.common.security.AuthUser;
import com.cmc.meeron.meeting.application.port.in.request.CreateMeetingRequestDto;
import com.cmc.meeron.meeting.application.port.in.request.DeleteMeetingRequestDto;
import com.cmc.meeron.meeting.application.port.in.request.DeleteMeetingRequestDtoBuilder;
import com.cmc.meeron.meeting.application.port.out.*;
import com.cmc.meeron.meeting.domain.Meeting;
import com.cmc.meeron.team.domain.Team;
import com.cmc.meeron.workspace.domain.Workspace;
import com.cmc.meeron.workspaceuser.domain.WorkspaceUser;
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
import static com.cmc.meeron.meeting.MeetingFixture.MEETING;
import static com.cmc.meeron.workspace.WorkspaceFixture.WORKSPACE_1;
import static com.cmc.meeron.workspace.WorkspaceFixture.WORKSPACE_2;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MeetingCommandServiceTest {

    @Mock
    MeetingToTeamQueryPort teamQueryPort;
    @Mock
    MeetingToWorkspaceQueryPort workspaceQueryPort;
    @Mock
    MeetingToWorkspaceUserQueryPort workspaceUserQueryPort;
    @Mock
    MeetingCommandPort meetingCommandPort;
    @Mock
    MeetingQueryPort meetingQueryPort;
    @InjectMocks
    MeetingCommandService meetingCommandService;

    private Meeting meeting;
    private AuthUser authUser;
    private Workspace workspace;

    @BeforeEach
    void setUp() {
        meeting = MEETING;
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
        when(workspaceUserQueryPort.findAllWorkspaceUsersByIds(createMeetingRequestDto.getMeetingAdminIds()))
                .thenReturn(Collections.emptyList());
        when(workspaceUserQueryPort.findByUserWorkspaceId(authUser.getUserId(), createMeetingRequestDto.getWorkspaceId()))
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
        when(workspaceUserQueryPort.findAllWorkspaceUsersByIds(any()))
                .thenReturn(List.of(WorkspaceUser.builder()
                        .workspace(WORKSPACE_2)
                        .id(1728L).build()));
        when(workspaceUserQueryPort.findByUserWorkspaceId(authUser.getUserId(), createMeetingRequestDto.getWorkspaceId()))
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
        when(workspaceUserQueryPort.findAllWorkspaceUsersByIds(any()))
                .thenReturn(List.of(WorkspaceUser.builder()
                        .workspace(workspace)
                        .id(1728L).build()));
        when(workspaceUserQueryPort.findByUserWorkspaceId(authUser.getUserId(), createMeetingRequestDto.getWorkspaceId()))
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
        when(workspaceUserQueryPort.findAllWorkspaceUsersByIds(createMeetingRequestDto.getMeetingAdminIds()))
                .thenReturn(workspaceUsers);
        when(workspaceUserQueryPort.findByUserWorkspaceId(authUser.getUserId(), createMeetingRequestDto.getWorkspaceId()))
                .thenReturn(Optional.of(WorkspaceUser.builder()
                        .id(1234L)
                        .workspace(workspace)
                        .user(authUser.getUser())
                        .build()));
        when(meetingCommandPort.saveMeeting(any(Meeting.class)))
                .thenReturn(1L);
        when(workspaceUserQueryPort.findByUserWorkspaceId(authUser.getUserId(), createMeetingRequestDto.getWorkspaceId()))
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
                () -> verify(workspaceUserQueryPort).findAllWorkspaceUsersByIds(createMeetingRequestDto.getMeetingAdminIds()),
                () -> verify(workspaceUserQueryPort).findByUserWorkspaceId(authUser.getUserId(), workspace.getId()),
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

    @DisplayName("회의 삭제 - 성공 / 이미 삭제된 경우")
    @Test
    void delete_meeting_success_not_found_meeting() throws Exception {

        // given
        DeleteMeetingRequestDto requestDto = DeleteMeetingRequestDtoBuilder.build();
        when(meetingQueryPort.findById(any()))
                .thenReturn(Optional.empty());

        // when, then
        assertAll(
                () -> assertDoesNotThrow(() -> meetingCommandService.deleteMeeting(requestDto)),
                () -> verify(meetingCommandPort, times(0)).deleteById(any())
        );
    }

    @DisplayName("회의 삭제 - 성공")
    @Test
    void delete_meeting_success() throws Exception {

        // given
        DeleteMeetingRequestDto requestDto = DeleteMeetingRequestDtoBuilder.build();
        when(meetingQueryPort.findById(any()))
                .thenReturn(Optional.of(meeting));

        // when
        meetingCommandService.deleteMeeting(requestDto);

        // then
        assertAll(
                () -> verify(meetingCommandPort).deleteById(meeting.getId())
        );
    }
}
