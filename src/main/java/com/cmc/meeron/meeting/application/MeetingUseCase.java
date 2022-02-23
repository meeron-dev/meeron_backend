package com.cmc.meeron.meeting.application;

import com.cmc.meeron.meeting.application.dto.request.TodayMeetingRequestDto;
import com.cmc.meeron.meeting.application.dto.response.TodayMeetingResponseDto;

import java.util.List;

public interface MeetingUseCase {

    List<TodayMeetingResponseDto> getTodayMeetings(TodayMeetingRequestDto todayMeetingRequestDto);
}
