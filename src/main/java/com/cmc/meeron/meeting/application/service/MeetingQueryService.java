package com.cmc.meeron.meeting.application.service;

import com.cmc.meeron.common.exception.meeting.MeetingNotFoundException;
import com.cmc.meeron.meeting.application.port.in.MeetingQueryUseCase;
import com.cmc.meeron.meeting.application.port.in.request.TodayMeetingRequestDto;
import com.cmc.meeron.meeting.application.port.in.response.MeetingResponseDto;
import com.cmc.meeron.meeting.application.port.in.response.TodayMeetingResponseDto;
import com.cmc.meeron.meeting.application.port.out.AgendaQueryPort;
import com.cmc.meeron.meeting.application.port.out.AttendeeQueryPort;
import com.cmc.meeron.meeting.application.port.out.MeetingQueryPort;
import com.cmc.meeron.meeting.application.port.out.response.AttendStatusCountQueryDto;
import com.cmc.meeron.meeting.application.port.out.response.MeetingAndAdminsQueryDto;
import com.cmc.meeron.meeting.domain.Agenda;
import com.cmc.meeron.meeting.domain.Attendee;
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
    private final AgendaQueryPort agendaQueryPort;

    @Override
    public List<TodayMeetingResponseDto> getTodayMeetings(TodayMeetingRequestDto todayMeetingRequestDto) {
        List<Meeting> todayMeetings = meetingQueryPort.findTodayMeetingsWithOperationTeam(todayMeetingRequestDto.getWorkspaceId(),
                todayMeetingRequestDto.getWorkspaceUserId());
        List<Long> meetingIds = todayMeetings.stream().map(Meeting::getId).collect(Collectors.toList());
        List<Agenda> agendas = agendaQueryPort.findByMeetingIds(meetingIds);
        List<Attendee> admins = attendeeQueryPort.findMeetingAdminsByMeetingIds(meetingIds);
        List<AttendStatusCountQueryDto> countsQueryDtos = attendeeQueryPort.countAttendStatusByMeetingIds(meetingIds);
        return TodayMeetingResponseDto.fromEntities(todayMeetings, agendas, admins, countsQueryDtos);
    }

    @Override
    public MeetingResponseDto getMeeting(Long meetingId) {
        MeetingAndAdminsQueryDto meetingAndAdminsQueryDto = meetingQueryPort.findWithTeamAndAdminsById(meetingId)
                .orElseThrow(MeetingNotFoundException::new);
        return MeetingResponseDto.fromQueryDto(meetingAndAdminsQueryDto);
    }
}
