package com.cmc.meeron.meeting.application.service;

import com.cmc.meeron.attendee.domain.Attendee;
import com.cmc.meeron.common.exception.meeting.MeetingNotFoundException;
import com.cmc.meeron.meeting.application.port.in.MeetingQueryUseCase;
import com.cmc.meeron.meeting.application.port.in.request.TodayMeetingRequestDto;
import com.cmc.meeron.meeting.application.port.in.response.MeetingResponseDto;
import com.cmc.meeron.meeting.application.port.in.response.TodayMeetingResponseDto;
import com.cmc.meeron.meeting.application.port.out.MeetingToAttendeeQueryPort;
import com.cmc.meeron.meeting.application.port.out.MeetingQueryPort;
import com.cmc.meeron.meeting.application.port.out.response.AttendStatusCountQueryDto;
import com.cmc.meeron.meeting.application.port.out.response.MeetingAndAdminsQueryDto;
import com.cmc.meeron.meeting.application.port.out.MeetingToAgendaQueryPort;
import com.cmc.meeron.meeting.domain.Meeting;
import com.cmc.meeron.topic.agenda.domain.Agenda;
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
    private final MeetingToAttendeeQueryPort meetingToAttendeeQueryPort;
    private final MeetingToAgendaQueryPort meetingToAgendaQueryPort;

    @Override
    public List<TodayMeetingResponseDto> getTodayMeetings(TodayMeetingRequestDto todayMeetingRequestDto) {
        List<Meeting> todayMeetings = meetingQueryPort.findTodayMeetingsWithOperationTeam(todayMeetingRequestDto.getWorkspaceId(),
                todayMeetingRequestDto.getWorkspaceUserId());
        if (todayMeetings.isEmpty()) {
            return TodayMeetingResponseDto.empty();
        }
        List<Long> meetingIds = todayMeetings.stream().map(Meeting::getId).collect(Collectors.toList());
        List<Agenda> agendas = meetingToAgendaQueryPort.findByMeetingIds(meetingIds);
        List<Attendee> admins = meetingToAttendeeQueryPort.findMeetingAdminsWithWorkspaceUserByMeetingIds(meetingIds);
        List<AttendStatusCountQueryDto> countsQueryDtos = meetingToAttendeeQueryPort.countAttendStatusByMeetingIds(meetingIds);
        return TodayMeetingResponseDto.fromEntities(todayMeetings, agendas, admins, countsQueryDtos);
    }

    @Override
    public MeetingResponseDto getMeeting(Long meetingId) {
        MeetingAndAdminsQueryDto meetingAndAdminsQueryDto = meetingQueryPort.findWithTeamAndAdminsById(meetingId)
                .orElseThrow(MeetingNotFoundException::new);
        return MeetingResponseDto.fromQueryDto(meetingAndAdminsQueryDto);
    }
}
