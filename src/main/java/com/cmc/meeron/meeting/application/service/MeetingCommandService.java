package com.cmc.meeron.meeting.application.service;

import com.cmc.meeron.common.exception.meeting.MeetingNotFoundException;
import com.cmc.meeron.common.exception.team.TeamNotFoundException;
import com.cmc.meeron.common.exception.user.WorkspaceUserNotFoundException;
import com.cmc.meeron.common.exception.workspace.WorkspaceNotFoundException;
import com.cmc.meeron.common.security.AuthUser;
import com.cmc.meeron.meeting.application.port.in.MeetingCommandUseCase;
import com.cmc.meeron.meeting.application.port.in.request.CreateAgendaRequestDto;
import com.cmc.meeron.meeting.application.port.in.request.CreateMeetingRequestDto;
import com.cmc.meeron.meeting.application.port.in.request.JoinAttendeesRequestDto;
import com.cmc.meeron.meeting.application.port.out.MeetingCommandPort;
import com.cmc.meeron.meeting.application.port.out.MeetingQueryPort;
import com.cmc.meeron.meeting.domain.Agenda;
import com.cmc.meeron.meeting.domain.Meeting;
import com.cmc.meeron.meeting.domain.MeetingInfo;
import com.cmc.meeron.meeting.domain.MeetingTime;
import com.cmc.meeron.team.application.port.out.TeamQueryPort;
import com.cmc.meeron.team.domain.Team;
import com.cmc.meeron.user.application.port.out.UserQueryPort;
import com.cmc.meeron.user.domain.WorkspaceUser;
import com.cmc.meeron.workspace.application.port.out.WorkspaceQueryPort;
import com.cmc.meeron.workspace.domain.Workspace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
class MeetingCommandService implements MeetingCommandUseCase {

    private final TeamQueryPort teamQueryPort;
    private final WorkspaceQueryPort workspaceQueryPort;
    private final UserQueryPort userQueryPort;
    private final MeetingCommandPort meetingCommandPort;
    private final MeetingQueryPort meetingQueryPort;

    @Override
    public Long createMeeting(CreateMeetingRequestDto createMeetingRequestDto, AuthUser authUser) {
        Team operationTeam = teamQueryPort.findById(createMeetingRequestDto.getOperationTeamId())
                .orElseThrow(TeamNotFoundException::new);
        Workspace workspace = workspaceQueryPort.findById(createMeetingRequestDto.getWorkspaceId())
                .orElseThrow(WorkspaceNotFoundException::new);
        List<WorkspaceUser> adminsIncludeMe = findAdminsAndMe(createMeetingRequestDto, authUser, workspace);
        validWorkspaceInTeamAndWorkspaceUsers(workspace, operationTeam, adminsIncludeMe);
        MeetingTime meetingTime = createMeetingRequestDto.createMeetingTime();
        MeetingInfo meetingInfo = createMeetingRequestDto.createMeetingInfo();
        Meeting meeting = Meeting.create(operationTeam, workspace, meetingTime, meetingInfo);
        meeting.addAdmins(adminsIncludeMe);
        return meetingCommandPort.saveMeeting(meeting);
    }

    private List<WorkspaceUser> findAdminsAndMe(CreateMeetingRequestDto createMeetingRequestDto, AuthUser authUser, Workspace workspace) {
        // TODO: 2022/03/13 kobeomseok95 쿼리를 두번치는건 조금 별로인듯 우선은 이렇게 진행하자.
        List<WorkspaceUser> admins = userQueryPort.findAllWorkspaceUsersByIds(createMeetingRequestDto.getMeetingAdminIds());
        WorkspaceUser me = userQueryPort.findByUserWorkspaceId(authUser.getUserId(), workspace.getId())
                .orElseThrow(() -> new WorkspaceUserNotFoundException("회의 생성자가 존재하지 않습니다."));
        List<WorkspaceUser> adminsIncludeMe = new ArrayList<>(admins);
        adminsIncludeMe.add(me);
        return adminsIncludeMe;
    }

    private void validWorkspaceInTeamAndWorkspaceUsers(Workspace workspace, Team operationTeam, List<WorkspaceUser> admins) {
        validWorkspaceAndWorkspaceUsers(workspace, admins);
        operationTeam.validWorkspace(workspace);
    }

    private void validWorkspaceAndWorkspaceUsers(Workspace workspace, List<WorkspaceUser> attendees) {
        attendees.forEach(attendee -> attendee.validInWorkspace(workspace));
    }

    @Override
    public void joinAttendees(JoinAttendeesRequestDto joinAttendeesRequestDto) {
        Meeting meeting = meetingQueryPort.findWithAttendeesById(joinAttendeesRequestDto.getMeetingId())
                .orElseThrow(MeetingNotFoundException::new);
        Workspace workspace = workspaceQueryPort.findById(meeting.getWorkspace().getId())
                .orElseThrow(WorkspaceNotFoundException::new);
        List<WorkspaceUser> attendees = userQueryPort.findAllWorkspaceUsersByIds(joinAttendeesRequestDto.getWorkspaceUserIds());
        validWorkspaceAndWorkspaceUsers(workspace, attendees);
        meeting.addAttendees(attendees);
    }

    @Override
    public List<Long> createAgendas(CreateAgendaRequestDto createAgendaRequestDto) {
        Meeting meeting = meetingQueryPort.findById(createAgendaRequestDto.getMeetingId())
                .orElseThrow(MeetingNotFoundException::new);
        return createAgendaRequestDto.getAgendaRequestDtos()
                .stream()
                .map(dto -> {
                    Agenda agenda = meetingCommandPort.saveAgenda(dto.createAgenda(meeting));
                    meetingCommandPort.saveIssues(dto.createIssues(agenda));
                    return agenda.getId();
                })
                .collect(Collectors.toList());
    }
}
