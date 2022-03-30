package com.cmc.meeron.meeting.application.service;

import com.cmc.meeron.meeting.application.port.in.MeetingQueryUseCase;
import com.cmc.meeron.meeting.application.port.in.request.TodayMeetingRequestDto;
import com.cmc.meeron.meeting.application.port.in.response.TodayMeetingResponseDto;
import com.cmc.meeron.meeting.application.port.out.AttendeeQueryPort;
import com.cmc.meeron.meeting.application.port.out.MeetingQueryPort;
import com.cmc.meeron.meeting.application.port.out.response.AttendStatusCountResponseDto;
import com.cmc.meeron.meeting.domain.Meeting;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class MeetingQueryService implements MeetingQueryUseCase {

    private final MeetingQueryPort meetingQueryPort;
    private final AttendeeQueryPort attendeeQueryPort;

    @Override
    public List<TodayMeetingResponseDto> getTodayMeetings(TodayMeetingRequestDto todayMeetingRequestDto) {
        List<Meeting> todayMeetings = meetingQueryPort.findTodayMeetings(todayMeetingRequestDto.getWorkspaceId(), todayMeetingRequestDto.getWorkspaceUserId());
        List<AttendStatusCountResponseDto> countResponseDtos = attendeeQueryPort.countAttendStatusByMeetingIds(todayMeetings
                .stream()
                .map(Meeting::getId)
                .collect(Collectors.toList()));
        return TodayMeetingResponseDto.fromEntitiesAndCounts(todayMeetings, countResponseDtos);
    }
}
