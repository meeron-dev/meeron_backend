package com.cmc.meeron.meeting.application.service;

import com.cmc.meeron.meeting.application.port.in.MeetingQueryUseCase;
import com.cmc.meeron.meeting.application.port.in.request.*;
import com.cmc.meeron.meeting.application.port.in.response.*;
import com.cmc.meeron.meeting.application.port.out.MeetingQueryPort;
import com.cmc.meeron.meeting.application.port.out.response.MonthMeetingsCountQueryDto;
import com.cmc.meeron.meeting.application.port.out.response.YearMeetingsCountQueryDto;
import com.cmc.meeron.meeting.domain.Meeting;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class MeetingQueryService implements MeetingQueryUseCase {

    private final MeetingQueryPort meetingQueryPort;

    @Override
    public List<TodayMeetingResponseDto> getTodayMeetings(TodayMeetingRequestDto todayMeetingRequestDto) {
        List<Meeting> todayMeetings = meetingQueryPort.findTodayMeetings(todayMeetingRequestDto.getWorkspaceId(), todayMeetingRequestDto.getWorkspaceUserId());
        return TodayMeetingResponseDto.ofList(todayMeetings);
    }

    @Override
    public List<Integer> getMeetingDays(MeetingDaysRequestDto meetingDaysRequestDto) {
        return meetingQueryPort.findMeetingDays(meetingDaysRequestDto.getSearchType(),
                meetingDaysRequestDto.getSearchIds(),
                meetingDaysRequestDto.getYearMonth());
    }

    @Override
    public List<WorkspaceAndTeamDayMeetingResponseDto> getWorkspaceAndTeamDayMeetings(DayMeetingsRequestDto dayMeetingsRequestDto) {
        List<Meeting> dayMeetings = findMeetings(dayMeetingsRequestDto);
        return WorkspaceAndTeamDayMeetingResponseDto.ofList(dayMeetings);
    }

    private List<Meeting> findMeetings(DayMeetingsRequestDto dayMeetingsRequestDto) {
        return meetingQueryPort.findDayMeetings(dayMeetingsRequestDto.getSearchType(),
                dayMeetingsRequestDto.getSearchIds(),
                dayMeetingsRequestDto.getLocalDate());
    }

    @Override
    public List<WorkspaceUserDayMeetingResponseDto> getWorkspaceUserDayMeetings(DayMeetingsRequestDto dayMeetingsRequestDto) {
        List<Meeting> meetings = findMeetings(dayMeetingsRequestDto);
        return WorkspaceUserDayMeetingResponseDto.ofList(meetings);
    }

    @Override
    public List<YearMeetingsCountResponseDto> getYearMeetingsCount(MeetingSearchRequestDto meetingSearchRequestDto) {
        List<YearMeetingsCountQueryDto> yearMeetingsCount = meetingQueryPort.findYearMeetingsCount(meetingSearchRequestDto.getSearchType(), meetingSearchRequestDto.getSearchIds());
        return YearMeetingsCountResponseDto.ofList(yearMeetingsCount);
    }

    @Override
    public List<MonthMeetingsCountResponseDto> getMonthMeetingsCount(MonthMeetingsCountRequestDto monthMeetingsCountRequestDto) {
        List<MonthMeetingsCountQueryDto> monthMeetingsCountQueryDtos = meetingQueryPort.findMonthMeetingsCount(monthMeetingsCountRequestDto.getSearchType(),
                monthMeetingsCountRequestDto.getSearchIds(),
                monthMeetingsCountRequestDto.getYear());
        return MonthMeetingsPerMonthMapper.toMonthMeetingsCountResponseDtoSortByMonth(monthMeetingsCountQueryDtos);
    }
}
