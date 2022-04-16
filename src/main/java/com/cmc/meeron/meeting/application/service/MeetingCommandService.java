package com.cmc.meeron.meeting.application.service;

import com.cmc.meeron.common.advice.attendee.CheckMeetingAdmin;
import com.cmc.meeron.common.exception.team.TeamNotFoundException;
import com.cmc.meeron.common.exception.workspace.WorkspaceNotFoundException;
import com.cmc.meeron.common.exception.workspace.WorkspaceUserNotFoundException;
import com.cmc.meeron.common.security.AuthUser;
import com.cmc.meeron.meeting.application.port.in.MeetingCommandUseCase;
import com.cmc.meeron.meeting.application.port.in.request.CreateMeetingRequestDto;
import com.cmc.meeron.meeting.application.port.in.request.DeleteMeetingRequestDto;
import com.cmc.meeron.meeting.application.port.out.MeetingCommandPort;
import com.cmc.meeron.meeting.application.port.out.MeetingQueryPort;
import com.cmc.meeron.meeting.domain.Meeting;
import com.cmc.meeron.meeting.domain.MeetingInfo;
import com.cmc.meeron.meeting.domain.MeetingTime;
import com.cmc.meeron.team.application.port.out.TeamQueryPort;
import com.cmc.meeron.team.domain.Team;
import com.cmc.meeron.workspace.application.port.out.WorkspaceQueryPort;
import com.cmc.meeron.workspace.application.port.out.WorkspaceUserQueryPort;
import com.cmc.meeron.workspace.domain.Workspace;
import com.cmc.meeron.workspace.domain.WorkspaceUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
class MeetingCommandService implements MeetingCommandUseCase {

    private final MeetingQueryPort meetingQueryPort;
    private final MeetingCommandPort meetingCommandPort;

    private final TeamQueryPort teamQueryPort;
    private final WorkspaceQueryPort workspaceQueryPort;
    private final WorkspaceUserQueryPort workspaceUserQueryPort;

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
        List<WorkspaceUser> admins = workspaceUserQueryPort.findAllWorkspaceUsersByIds(createMeetingRequestDto.getMeetingAdminIds());
        WorkspaceUser me = workspaceUserQueryPort.findByUserWorkspaceId(authUser.getUserId(), workspace.getId())
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

    @CheckMeetingAdmin
    @Override
    public void deleteMeeting(DeleteMeetingRequestDto deleteMeetingRequestDto) {
        meetingQueryPort.findById(deleteMeetingRequestDto.getMeetingId())
                .ifPresent(meeting -> meetingCommandPort.deleteById(meeting.getId()));
    }
}
