package com.cmc.meeron.meeting.application.port.out;

import com.cmc.meeron.meeting.domain.Meeting;

public interface MeetingCommandPort {

    Long save(Meeting meeting);
}
