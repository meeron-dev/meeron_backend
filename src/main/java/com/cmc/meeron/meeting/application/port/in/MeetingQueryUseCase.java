package com.cmc.meeron.meeting.application.port.in;

import com.cmc.meeron.meeting.application.port.in.request.*;
import com.cmc.meeron.meeting.application.port.in.response.*;

import java.util.List;

public interface MeetingQueryUseCase {

    List<TodayMeetingResponseDto> getTodayMeetings(TodayMeetingRequestDto todayMeetingRequestDto);

    MeetingResponseDto getMeeting(Long meetingId);
}
