package com.cmc.meeron.attendee.application.service;

import com.cmc.meeron.attendee.application.port.in.AttendeeCommandUseCase;
import com.cmc.meeron.attendee.application.port.in.request.ChangeAttendStatusRequestDto;
import com.cmc.meeron.attendee.application.port.in.request.JoinAttendeesRequestDto;
import com.cmc.meeron.attendee.application.port.out.AttendeeQueryPort;
import com.cmc.meeron.attendee.application.port.out.AttendeeToMeetingQueryPort;
import com.cmc.meeron.attendee.domain.AttendStatus;
import com.cmc.meeron.attendee.domain.Attendee;
import com.cmc.meeron.common.exception.meeting.AttendeeNotFoundException;
import com.cmc.meeron.common.exception.meeting.MeetingNotFoundException;
import com.cmc.meeron.meeting.domain.Attendees;
import com.cmc.meeron.meeting.domain.Meeting;
import com.cmc.meeron.workspaceuser.domain.WorkspaceUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
class AttendeeCommandService implements AttendeeCommandUseCase {

    private final AttendeeToMeetingQueryPort attendeeToMeetingQueryPort;
    private final AttendeeQueryPort attendeeQueryPort;
    private final JoinMeetingValidator joinMeetingValidator;

    @Override
    public void joinAttendees(JoinAttendeesRequestDto joinAttendeesRequestDto) {
        Meeting meeting = attendeeToMeetingQueryPort.findWithAttendeesById(joinAttendeesRequestDto.getMeetingId())
                .orElseThrow(MeetingNotFoundException::new);
        List<WorkspaceUser> attendWorkspaceUsers = joinMeetingValidator
                .workspaceUsersInEqualWorkspace(joinAttendeesRequestDto.getWorkspaceUserIds(), meeting.getWorkspace());
        meeting.addAttendees(attendWorkspaceUsers);
    }

    @Deprecated
    @Override
    public void changeAttendStatus(ChangeAttendStatusRequestDto changeAttendStatusRequestDto) {
        Attendees attendees = attendeeToMeetingQueryPort.findWithAttendeesById(changeAttendStatusRequestDto.getMeetingId())
                .orElseThrow(MeetingNotFoundException::new)
                .getAttendees();
        Attendee attendee = attendees.findByWorkspaceUserId(changeAttendStatusRequestDto.getWorkspaceUserId());
        attendee.changeStatus(AttendStatus.valueOf(changeAttendStatusRequestDto.getStatus()));
    }

    @Override
    public void changeAttendStatusV2(ChangeAttendStatusRequestDto changeAttendStatusRequestDto) {
        Attendee attendee = attendeeQueryPort.findById(changeAttendStatusRequestDto.getAttendeeId())
                .orElseThrow(AttendeeNotFoundException::new);
        attendee.changeStatus(AttendStatus.valueOf(changeAttendStatusRequestDto.getStatus()));
    }
}
