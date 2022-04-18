package com.cmc.meeron.attendee.application.port.out;

import com.cmc.meeron.meeting.domain.Meeting;

import java.util.Optional;

public interface AttendeeToMeetingQueryPort {

    Optional<Meeting> findWithAttendeesById(Long meetingId);
}
