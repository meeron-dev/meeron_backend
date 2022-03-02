package com.cmc.meeron.meeting.application;

import com.cmc.meeron.meeting.application.dto.request.*;
import com.cmc.meeron.meeting.application.dto.response.WorkspaceAndTeamDayMeetingResponseDto;
import com.cmc.meeron.meeting.application.dto.response.TodayMeetingResponseDto;
import com.cmc.meeron.meeting.application.dto.response.WorkspaceUserDayMeetingResponseDto;
import com.cmc.meeron.meeting.domain.Meeting;
import com.cmc.meeron.meeting.domain.MeetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class MeetingService implements MeetingQueryUseCase {

    private final MeetingRepository meetingRepository;

    @Override
    public List<TodayMeetingResponseDto> getTodayMeetings(TodayMeetingRequestDto todayMeetingRequestDto) {
        List<Meeting> todayMeetings = meetingRepository.findTodayMeetings(todayMeetingRequestDto.getWorkspaceId(), todayMeetingRequestDto.getWorkspaceUserId());
        return MeetingApplicationAssembler.toTodayMeetingResponseDtos(todayMeetings);
    }

    @Override
    public List<Integer> getMeetingDays(MeetingDaysRequestDto meetingDaysRequestDto) {
        return meetingRepository.findMeetingDays(meetingDaysRequestDto.getSearchType(),
                meetingDaysRequestDto.getSearchIds(),
                meetingDaysRequestDto.getYearMonth());
    }

    @Override
    public List<WorkspaceAndTeamDayMeetingResponseDto> getWorkspaceAndTeamDayMeetings(DayMeetingsRequestDto dayMeetingsRequestDto) {
        List<Meeting> dayMeetings = findMeetings(dayMeetingsRequestDto);
        return MeetingApplicationAssembler.toWorkspaceAndTeamMeetingResponseDtos(dayMeetings);
    }

    private List<Meeting> findMeetings(DayMeetingsRequestDto dayMeetingsRequestDto) {
        return meetingRepository.findDayMeetings(dayMeetingsRequestDto.getSearchType(),
                dayMeetingsRequestDto.getSearchIds(),
                dayMeetingsRequestDto.getLocalDate());
    }

    @Override
    public List<WorkspaceUserDayMeetingResponseDto> getWorkspaceUserDayMeetings(DayMeetingsRequestDto dayMeetingsRequestDto) {
        List<Meeting> meetings = findMeetings(dayMeetingsRequestDto);
        return MeetingApplicationAssembler.toWorkspaceUserDayMeetingResponseDtos(meetings);
    }
}
