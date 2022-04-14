package com.cmc.meeron.meeting.application.advisor;

import com.cmc.meeron.common.advice.attendee.AttendeeAuthorityCheckable;
import com.cmc.meeron.common.exception.meeting.AttendeeNotFoundException;
import com.cmc.meeron.meeting.application.port.out.AttendeeQueryPort;
import com.cmc.meeron.attendee.domain.Attendee;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
class CheckMeetingAdminAdvisor {

    private final AttendeeQueryPort attendeeQueryPort;

    @Before("@annotation(com.cmc.meeron.common.advice.attendee.CheckMeetingAdmin)")
    public void checkAdminWorkspaceUser(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        AttendeeAuthorityCheckable checkable = (AttendeeAuthorityCheckable) args[0];
        Attendee attendee = attendeeQueryPort.findByMeetingIdAndWorkspaceUserId(checkable.getMeetingId(),
                checkable.getWorkspaceUserId())
                .orElseThrow(AttendeeNotFoundException::new);
        attendee.isAdminOrThrow();
    }
}
