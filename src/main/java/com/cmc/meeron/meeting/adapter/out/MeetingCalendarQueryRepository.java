package com.cmc.meeron.meeting.adapter.out;

import com.cmc.meeron.meeting.application.port.out.MeetingMyCalendarQueryPort;
import com.cmc.meeron.meeting.application.port.out.MeetingTeamCalendarQueryPort;
import com.cmc.meeron.meeting.application.port.out.MeetingWorkspaceCalendarQueryPort;
import com.cmc.meeron.meeting.application.port.out.response.MonthMeetingsCountQueryDto;
import com.cmc.meeron.meeting.application.port.out.response.YearMeetingsCountQueryDto;
import com.cmc.meeron.meeting.domain.Meeting;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.util.List;

@Repository
@RequiredArgsConstructor
class MeetingCalendarQueryRepository implements MeetingWorkspaceCalendarQueryPort,
        MeetingTeamCalendarQueryPort,
        MeetingMyCalendarQueryPort {

    private final MeetingQuerydslRepository meetingQuerydslRepository;

    @Override
    public List<Integer> findMyMeetingDays(List<Long> myWorkspaceUserIds, YearMonth yearMonth) {
        return meetingQuerydslRepository.findMyMeetingDays(myWorkspaceUserIds, yearMonth);
    }

    @Override
    public List<Meeting> findMyDayMeetings(List<Long> myWorkspaceUserIds, LocalDate localDate) {
        return meetingQuerydslRepository.findMyDayMeetings(myWorkspaceUserIds, localDate);
    }

    @Override
    public List<YearMeetingsCountQueryDto> findMyYearMeetingsCount(List<Long> myWorkspaceUserIds) {
        return meetingQuerydslRepository.findMyYearMeetingsCount(myWorkspaceUserIds);
    }

    @Override
    public List<MonthMeetingsCountQueryDto> findMyMonthMeetingsCount(List<Long> myWorkspaceUserIds, Year year) {
        return meetingQuerydslRepository.findMyMonthMeetingsCount(myWorkspaceUserIds, year);
    }

    @Override
    public List<Integer> findTeamMeetingDays(Long teamId, YearMonth yearMonth) {
        return meetingQuerydslRepository.findTeamMeetingDays(teamId, yearMonth);
    }

    @Override
    public List<Meeting> findTeamDayMeetings(Long teamId, LocalDate localDate) {
        return meetingQuerydslRepository.findTeamDayMeetings(teamId, localDate);
    }

    @Override
    public List<YearMeetingsCountQueryDto> findTeamYearMeetingsCount(Long teamId) {
        return meetingQuerydslRepository.findTeamYearMeetingsCount(teamId);
    }

    @Override
    public List<MonthMeetingsCountQueryDto> findTeamMonthMeetingsCount(Long teamId, Year year) {
        return meetingQuerydslRepository.findTeamMonthMeetingsCount(teamId, year);
    }

    @Override
    public List<Integer> findWorkspaceMeetingDays(Long workspaceUserId, YearMonth yearMonth) {
        return meetingQuerydslRepository.findWorkspaceMeetingDays(workspaceUserId, yearMonth);
    }

    @Override
    public List<Meeting> findWorkspaceDayMeetings(Long workspaceUserId, LocalDate localDate) {
        return meetingQuerydslRepository.findWorkspaceDayMeetings(workspaceUserId, localDate);
    }

    @Override
    public List<YearMeetingsCountQueryDto> findWorkspaceYearMeetingsCount(Long workspaceUserId) {
        return meetingQuerydslRepository.findWorkspaceYearMeetingsCount(workspaceUserId);
    }

    @Override
    public List<MonthMeetingsCountQueryDto> findWorkspaceMonthMeetingsCount(Long workspaceUserId, Year year) {
        return meetingQuerydslRepository.findWorkspaceMonthMeetingsCount(workspaceUserId, year);
    }
}
