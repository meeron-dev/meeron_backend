package com.cmc.meeron.meeting.application.service;

import com.cmc.meeron.meeting.application.port.in.MeetingCalendarQueryUseCase;
import com.cmc.meeron.meeting.application.port.in.response.DayMeetingResponseDto;
import com.cmc.meeron.meeting.application.port.in.response.MonthMeetingsCountResponseDto;
import com.cmc.meeron.meeting.application.port.in.response.YearMeetingsCountResponseDto;
import com.cmc.meeron.meeting.application.port.out.MeetingMyCalendarQueryPort;
import com.cmc.meeron.meeting.application.port.out.response.MonthMeetingsCountQueryDto;
import com.cmc.meeron.meeting.application.port.out.response.YearMeetingsCountQueryDto;
import com.cmc.meeron.meeting.domain.Meeting;
import com.cmc.meeron.workspace.application.port.out.WorkspaceUserQueryPort;
import com.cmc.meeron.workspace.domain.WorkspaceUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class MeetingMyCalendarQueryService implements MeetingCalendarQueryUseCase {

    private final WorkspaceUserQueryPort workspaceUserQueryPort;
    private final MeetingMyCalendarQueryPort meetingMyCalendarQueryPort;

    @Override
    public String getType() {
        return "WORKSPACE_USER";
    }

    @Override
    public List<Integer> getMeetingDays(Long userId, YearMonth yearMonth) {
        return meetingMyCalendarQueryPort.findMyMeetingDays(getMyWorkspaceUserIds(userId), yearMonth);
    }

    private List<Long> getMyWorkspaceUserIds(Long userId) {
        List<WorkspaceUser> workspaceUsers = workspaceUserQueryPort.findMyWorkspaceUsers(userId);
        return workspaceUsers.stream().map(WorkspaceUser::getId).collect(Collectors.toList());
    }

    @Override
    public List<DayMeetingResponseDto> getDayMeetings(Long userId, LocalDate localDate) {
        List<Meeting> meetings = meetingMyCalendarQueryPort.findMyDayMeetings(getMyWorkspaceUserIds(userId), localDate);
        return DayMeetingResponseDto.fromEntitiesMeetingWithWorkspace(meetings);
    }

    @Override
    public List<YearMeetingsCountResponseDto> getMeetingCountPerYear(Long userId) {
        List<YearMeetingsCountQueryDto> yearMeetingsCount = meetingMyCalendarQueryPort.findMyYearMeetingsCount(getMyWorkspaceUserIds(userId));
        return YearMeetingsCountResponseDto.fromQueryResponseDtos(yearMeetingsCount);
    }

    @Override
    public List<MonthMeetingsCountResponseDto> getMeetingCountPerMonth(Long userId, Year year) {
        List<MonthMeetingsCountQueryDto> monthMeetingsCount = meetingMyCalendarQueryPort.findMyMonthMeetingsCount(getMyWorkspaceUserIds(userId), year);
        return MonthMeetingsPerMonthMapper.fromQueryResponseDtos(monthMeetingsCount);
    }
}
