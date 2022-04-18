package com.cmc.meeron.attendee.application.service;

import com.cmc.meeron.attendee.application.port.in.AttendeeCommandUseCase;
import com.cmc.meeron.attendee.application.port.in.request.ChangeAttendStatusRequestDto;
import com.cmc.meeron.attendee.application.port.in.request.JoinAttendeesRequestDto;
import com.cmc.meeron.attendee.application.port.out.AttendeeToMeetingQueryPort;
import com.cmc.meeron.attendee.domain.AttendStatus;
import com.cmc.meeron.attendee.domain.Attendee;
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
    private final JoinMeetingValidator joinMeetingValidator;

    @Override
    public void joinAttendees(JoinAttendeesRequestDto joinAttendeesRequestDto) {
        Meeting meeting = attendeeToMeetingQueryPort.findWithAttendeesById(joinAttendeesRequestDto.getMeetingId())
                .orElseThrow(MeetingNotFoundException::new);
        List<WorkspaceUser> attendWorkspaceUsers = joinMeetingValidator
                .workspaceUsersInEqualWorkspace(joinAttendeesRequestDto.getWorkspaceUserIds(), meeting.getWorkspace());
        meeting.addAttendees(attendWorkspaceUsers);
    }

    // FIXME: 2022/04/14 kobeomseok95 논의 후 attendeeId로 파라미터 변경
    @Override
    public void changeAttendStatus(ChangeAttendStatusRequestDto changeAttendStatusRequestDto) {
        Attendees attendees = attendeeToMeetingQueryPort.findWithAttendeesById(changeAttendStatusRequestDto.getMeetingId())
                .orElseThrow(MeetingNotFoundException::new)
                .getAttendees();
        Attendee attendee = attendees.findByWorkspaceUserId(changeAttendStatusRequestDto.getWorkspaceUserId());
        attendee.changeStatus(AttendStatus.valueOf(changeAttendStatusRequestDto.getStatus()));
    }
}
