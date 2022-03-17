package com.cmc.meeron.meeting.application.service;

import com.cmc.meeron.meeting.application.port.in.MeetingCalendarQueryUseCase;
import com.cmc.meeron.meeting.application.port.in.response.DayMeetingResponseDto;
import com.cmc.meeron.meeting.application.port.in.response.MonthMeetingsCountResponseDto;
import com.cmc.meeron.meeting.application.port.in.response.YearMeetingsCountResponseDto;
import com.cmc.meeron.meeting.application.port.out.MeetingTeamCalendarQueryPort;
import com.cmc.meeron.meeting.application.port.out.response.MonthMeetingsCountQueryDto;
import com.cmc.meeron.meeting.application.port.out.response.YearMeetingsCountQueryDto;
import com.cmc.meeron.meeting.domain.Meeting;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class MeetingTeamCalendarQueryService implements MeetingCalendarQueryUseCase {

    private final MeetingTeamCalendarQueryPort meetingTeamCalendarQueryPort;

    @Override
    public String getType() {
        return "TEAM";
    }

    @Override
    public List<Integer> getMeetingDays(Long teamId, YearMonth yearMonth) {
        return meetingTeamCalendarQueryPort.findTeamMeetingDays(teamId, yearMonth);
    }

    @Override
    public List<DayMeetingResponseDto> getDayMeetings(Long teamId, LocalDate localDate) {
        List<Meeting> meetings = meetingTeamCalendarQueryPort.findTeamDayMeetings(teamId, localDate);
        return DayMeetingResponseDto.fromEntities(meetings);
    }

    @Override
    public List<YearMeetingsCountResponseDto> getMeetingCountPerYear(Long teamId) {
        List<YearMeetingsCountQueryDto> yearMeetingsCount = meetingTeamCalendarQueryPort.findTeamYearMeetingsCount(teamId);
        return YearMeetingsCountResponseDto.fromQueryResponseDtos(yearMeetingsCount);
    }

    @Override
    public List<MonthMeetingsCountResponseDto> getMeetingCountPerMonth(Long teamId, Year year) {
        List<MonthMeetingsCountQueryDto> monthMeetingsCount = meetingTeamCalendarQueryPort.findTeamMonthMeetingsCount(teamId, year);
        return MonthMeetingsPerMonthMapper.fromQueryResponseDtos(monthMeetingsCount);
    }
}
