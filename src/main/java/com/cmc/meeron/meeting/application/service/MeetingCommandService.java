package com.cmc.meeron.meeting.application.service;

import com.cmc.meeron.common.exception.meeting.MeetingNotFoundException;
import com.cmc.meeron.common.exception.meeting.NotWorkspacesTeamException;
import com.cmc.meeron.common.exception.team.TeamNotFoundException;
import com.cmc.meeron.common.exception.workspace.WorkspaceUsersNotInEqualWorkspaceException;
import com.cmc.meeron.meeting.application.port.in.MeetingCommandUseCase;
import com.cmc.meeron.meeting.application.port.in.request.CreateMeetingRequestDto;
import com.cmc.meeron.meeting.application.port.in.request.JoinAttendeesRequestDto;
import com.cmc.meeron.meeting.application.port.out.MeetingCommandPort;
import com.cmc.meeron.meeting.application.port.out.MeetingQueryPort;
import com.cmc.meeron.meeting.domain.Meeting;
import com.cmc.meeron.meeting.domain.MeetingBasicInfoVo;
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

import java.util.List;

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
    public Long creteMeeting(CreateMeetingRequestDto createMeetingRequestDto) {
        List<Long> workspaceUserIds = createMeetingRequestDto.getMeetingManagerIds();

        Team operationTeam = teamQueryPort.findById(createMeetingRequestDto.getOperationTeamId())
                .orElseThrow(TeamNotFoundException::new);
        Workspace workspace = validateEqualWorkspace(workspaceUserIds, operationTeam.getWorkspace());
        List<WorkspaceUser> meetingAdmins = findWorkspaceUsers(workspaceUserIds);

        MeetingTime meetingTime = createMeetingRequestDto.createMeetingTime();
        MeetingBasicInfoVo meetingBasicInfoVo = createMeetingRequestDto.createMeetingBasicInfo();
        Meeting meeting = Meeting.create(operationTeam, workspace, meetingTime, meetingBasicInfoVo);
        meeting.addAdmins(meetingAdmins);
        return meetingCommandPort.save(meeting);
    }

    private List<WorkspaceUser> findWorkspaceUsers(List<Long> workspaceUserIds) {
        return userQueryPort.findAllWorkspaceUsersById(workspaceUserIds);
    }

    private Workspace validateEqualWorkspace(List<Long> workspaceUserIds, Workspace needValidWorkspace) {
        List<Workspace> workspaces = workspaceQueryPort.findByWorkspaceUserIds(workspaceUserIds);
        if (workspaces.size() > 1) {
            throw new WorkspaceUsersNotInEqualWorkspaceException();
        }
        Workspace workspace = workspaces.get(0);
        if (!workspace.equals(needValidWorkspace)) {
            throw new NotWorkspacesTeamException();
        }
        return workspace;
    }

    @Override
    public void joinAttendees(JoinAttendeesRequestDto joinAttendeesRequestDto) {
        Meeting meeting = meetingQueryPort.findById(joinAttendeesRequestDto.getMeetingId())
                .orElseThrow(MeetingNotFoundException::new);
        validateEqualWorkspace(joinAttendeesRequestDto.getWorkspaceUserIds(), meeting.getWorkspace());
        List<WorkspaceUser> attendees = findWorkspaceUsers(joinAttendeesRequestDto.getWorkspaceUserIds());
        meeting.addAttendees(attendees);
    }
}
