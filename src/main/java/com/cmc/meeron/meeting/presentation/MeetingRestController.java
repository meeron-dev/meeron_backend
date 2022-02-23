package com.cmc.meeron.meeting.presentation;

import com.cmc.meeron.meeting.application.MeetingUseCase;
import com.cmc.meeron.meeting.application.dto.response.TodayMeetingResponseDto;
import com.cmc.meeron.meeting.presentation.dto.request.TodayMeetingRequest;
import com.cmc.meeron.meeting.presentation.dto.response.TodayMeetingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/meetings")
public class MeetingRestController {

    private final MeetingUseCase meetingUseCase;

    @GetMapping("/today")
    @ResponseStatus(HttpStatus.OK)
    public TodayMeetingResponse todayMeetings(@Valid TodayMeetingRequest todayMeetingRequest) {
        List<TodayMeetingResponseDto> todayMeetings = meetingUseCase.getTodayMeetings(todayMeetingRequest.toDto());
        return TodayMeetingResponse.fromDto(todayMeetings);
    }
}
