package com.cmc.meeron.meeting.application.service;

import com.cmc.meeron.common.exception.meeting.MeetingNotFoundException;
import com.cmc.meeron.meeting.application.port.in.MeetingQueryUseCase;
import com.cmc.meeron.meeting.application.port.in.request.TodayMeetingRequestDto;
import com.cmc.meeron.meeting.application.port.in.response.MeetingResponseDto;
import com.cmc.meeron.meeting.application.port.in.response.TodayMeetingResponseDto;
import com.cmc.meeron.meeting.application.port.out.AttendeeQueryPort;
import com.cmc.meeron.meeting.application.port.out.MeetingQueryPort;
import com.cmc.meeron.meeting.application.port.out.response.AttendStatusCountQueryDto;
import com.cmc.meeron.meeting.application.port.out.response.MeetingAndAdminsQueryDto;
import com.cmc.meeron.meeting.application.port.out.response.TodayMeetingsQueryDto;
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
        List<TodayMeetingsQueryDto> todayMeetingsQueryDtos = meetingQueryPort
                .findTodayMeetings(todayMeetingRequestDto.getWorkspaceId(), todayMeetingRequestDto.getWorkspaceUserId());
        List<AttendStatusCountQueryDto> countResponseDtos = attendeeQueryPort.countAttendStatusByMeetingIds(todayMeetingsQueryDtos
                .stream()
                .map(TodayMeetingsQueryDto::getMeetingId)
                .collect(Collectors.toList()));
        return TodayMeetingResponseDto.fromQueryDtos(todayMeetingsQueryDtos, countResponseDtos);
    }

    @Override
    public MeetingResponseDto getMeeting(Long meetingId) {
        MeetingAndAdminsQueryDto meetingAndAdminsQueryDto = meetingQueryPort.findWithTeamAndAdminsById(meetingId)
                .orElseThrow(MeetingNotFoundException::new);
        return MeetingResponseDto.fromQueryDto(meetingAndAdminsQueryDto);
    }
}
