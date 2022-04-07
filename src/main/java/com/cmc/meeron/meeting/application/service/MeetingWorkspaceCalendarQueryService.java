package com.cmc.meeron.meeting.application.service;

import com.cmc.meeron.common.exception.workspace.WorkspaceNotFoundException;
import com.cmc.meeron.common.security.SecurityUtil;
import com.cmc.meeron.meeting.application.port.in.MeetingCalendarQueryUseCase;
import com.cmc.meeron.meeting.application.port.in.response.DayMeetingResponseDto;
import com.cmc.meeron.meeting.application.port.in.response.MonthMeetingsCountResponseDto;
import com.cmc.meeron.meeting.application.port.in.response.YearMeetingsCountResponseDto;
import com.cmc.meeron.meeting.application.port.out.MeetingWorkspaceCalendarQueryPort;
import com.cmc.meeron.meeting.application.port.out.response.MonthMeetingsCountQueryDto;
import com.cmc.meeron.meeting.application.port.out.response.YearMeetingsCountQueryDto;
import com.cmc.meeron.meeting.domain.Meeting;
import com.cmc.meeron.workspace.application.port.out.WorkspaceUserQueryPort;
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
class MeetingWorkspaceCalendarQueryService implements MeetingCalendarQueryUseCase {

    private final WorkspaceUserQueryPort workspaceUserQueryPort;
    private final MeetingWorkspaceCalendarQueryPort meetingWorkspaceCalendarQueryPort;

    @Override
    public String getType() {
        return "WORKSPACE";
    }

    @Override
    public List<Integer> getMeetingDays(Long workspaceId, YearMonth yearMonth) {
        return meetingWorkspaceCalendarQueryPort.findWorkspaceMeetingDays(
                getMyWorkspaceUserId(SecurityUtil.getUserId(), workspaceId),
                yearMonth);
    }

    private Long getMyWorkspaceUserId(Long userId, Long workspaceId) {
        return workspaceUserQueryPort.findByUserWorkspaceId(userId, workspaceId)
                .orElseThrow(WorkspaceNotFoundException::new)
                .getId();
    }

    @Override
    public List<DayMeetingResponseDto> getDayMeetings(Long workspaceId, LocalDate localDate) {
        List<Meeting> meetings = meetingWorkspaceCalendarQueryPort.findWorkspaceDayMeetings(
                getMyWorkspaceUserId(SecurityUtil.getUserId(), workspaceId),
                localDate
        );
        return DayMeetingResponseDto.fromEntitiesOnlyMeeting(meetings);
    }

    @Override
    public List<YearMeetingsCountResponseDto> getMeetingCountPerYear(Long workspaceId) {
        List<YearMeetingsCountQueryDto> yearMeetingsCount =
                meetingWorkspaceCalendarQueryPort.findWorkspaceYearMeetingsCount(getMyWorkspaceUserId(SecurityUtil.getUserId(), workspaceId));
        return YearMeetingsCountResponseDto.fromQueryResponseDtos(yearMeetingsCount);
    }

    @Override
    public List<MonthMeetingsCountResponseDto> getMeetingCountPerMonth(Long workspaceId, Year year) {
        List<MonthMeetingsCountQueryDto> monthMeetingsCount = meetingWorkspaceCalendarQueryPort.findWorkspaceMonthMeetingsCount(
                getMyWorkspaceUserId(SecurityUtil.getUserId(), workspaceId),
                year
        );
        return MonthMeetingsPerMonthMapper.fromQueryResponseDtos(monthMeetingsCount);
    }
}
