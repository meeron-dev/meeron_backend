package com.cmc.meeron.meeting.application;

import com.cmc.meeron.meeting.application.dto.request.*;
import com.cmc.meeron.meeting.application.dto.response.*;
import com.cmc.meeron.meeting.domain.Meeting;
import com.cmc.meeron.meeting.domain.MeetingRepository;
import com.cmc.meeron.meeting.domain.dto.MonthMeetingsCountDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public List<YearMeetingsCountResponseDto> getYearMeetingsCount(MeetingSearchRequestDto meetingSearchRequestDto) {
        return meetingRepository.findYearMeetingsCount(meetingSearchRequestDto.getSearchType(), meetingSearchRequestDto.getSearchIds())
                .stream()
                .map(it -> YearMeetingsCountResponseDto.builder()
                        .year(it.getYear())
                        .count(it.getCount())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<MonthMeetingsCountResponseDto> getMonthMeetingsCount(MonthMeetingsCountRequestDto monthMeetingsCountRequestDto) {
        List<MonthMeetingsCountDto> monthMeetingsCountDtos = meetingRepository.findMonthMeetingsCount(monthMeetingsCountRequestDto.getSearchType(),
                monthMeetingsCountRequestDto.getSearchIds(),
                monthMeetingsCountRequestDto.getYear());
        return MeetingApplicationAssembler.toMonthMeetingsCountResponseDtoSortByMonth(monthMeetingsCountDtos);
    }
}
