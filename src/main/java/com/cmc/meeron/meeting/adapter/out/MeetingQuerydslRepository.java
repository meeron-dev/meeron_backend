package com.cmc.meeron.meeting.adapter.out;

import com.cmc.meeron.common.exception.meeting.MeetingNotFoundException;
import com.cmc.meeron.meeting.application.port.out.response.*;
import com.cmc.meeron.meeting.domain.Meeting;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

import static com.cmc.meeron.meeting.domain.QAttendee.attendee;
import static com.cmc.meeron.meeting.domain.QMeeting.meeting;
import static com.cmc.meeron.team.domain.QTeam.team;
import static com.cmc.meeron.workspace.domain.QWorkspace.workspace;
import static com.cmc.meeron.workspace.domain.QWorkspaceUser.workspaceUser;

@Repository
@RequiredArgsConstructor
class MeetingQuerydslRepository {

    private final JPAQueryFactory queryFactory;

    public List<Integer> findMyMeetingDays(List<Long> myWorkspaceUserIds, YearMonth yearMonth) {
        return queryFactory.select(meeting.meetingTime.startDate.dayOfMonth())
                .from(attendee)
                .join(attendee.meeting, meeting)
                .where(attendee.workspaceUser.id.in(myWorkspaceUserIds),
                        yearMonthEq(yearMonth))
                .fetch();
    }

    private BooleanExpression yearMonthEq(YearMonth yearMonth) {
        return meeting.meetingTime.startDate.year().eq(yearMonth.getYear())
                .and(meeting.meetingTime.startDate.month().eq(yearMonth.getMonthValue()));
    }

    public List<Integer> findTeamMeetingDays(Long teamId, YearMonth yearMonth) {
        return queryFactory.select(meeting.meetingTime.startDate.dayOfMonth())
                .from(meeting)
                .where(meeting.team.id.eq(teamId),
                        yearMonthEq(yearMonth))
                .orderBy(meeting.meetingTime.startDate.dayOfMonth().asc())
                .fetch();
    }

    public List<Integer> findWorkspaceMeetingDays(Long workspaceUserId, YearMonth yearMonth) {
        return queryFactory.select(meeting.meetingTime.startDate.dayOfMonth())
                .from(attendee)
                .join(attendee.meeting, meeting)
                .where(attendee.workspaceUser.id.eq(workspaceUserId),
                        yearMonthEq(yearMonth))
                .fetch();
    }

    public List<Meeting> findMyDayMeetings(List<Long> myWorkspaceUserIds, LocalDate localDate) {
        return queryFactory.selectFrom(meeting)
                .join(meeting.workspace, workspace).fetchJoin()
                .join(meeting.attendees.values, attendee)
                .where(attendee.workspaceUser.id.in(myWorkspaceUserIds),
                        dateEq(localDate))
                .fetch();
    }

    private BooleanExpression dateEq(LocalDate date) {
        return meeting.meetingTime.startDate.eq(date);
    }

    public List<Meeting> findTeamDayMeetings(Long teamId, LocalDate localDate) {
        return queryFactory.selectFrom(meeting)
                .where(meeting.team.id.eq(teamId),
                        dateEq(localDate))
                .fetch();
    }

    public List<Meeting> findWorkspaceDayMeetings(Long workspaceUserId, LocalDate localDate) {
        return queryFactory.selectFrom(meeting)
                .join(meeting.workspace, workspace).fetchJoin()
                .join(meeting.attendees.values, attendee)
                .where(attendee.workspaceUser.id.eq(workspaceUserId),
                        dateEq(localDate))
                .fetch();
    }

    public List<YearMeetingsCountQueryDto> findMyYearMeetingsCount(List<Long> myWorkspaceUserIds) {
        return queryFactory.select(new QYearMeetingsCountQueryDto(meeting.meetingTime.startDate.year(),
                meeting.meetingTime.startDate.count()))
                .from(attendee)
                .join(attendee.meeting, meeting)
                .where(attendee.workspaceUser.id.in(myWorkspaceUserIds))
                .groupBy(meeting.meetingTime.startDate.year())
                .orderBy(meeting.meetingTime.startDate.year().desc())
                .fetch();
    }

    public List<YearMeetingsCountQueryDto> findTeamYearMeetingsCount(Long teamId) {
        return queryFactory.select(new QYearMeetingsCountQueryDto(meeting.meetingTime.startDate.year(),
                meeting.meetingTime.startDate.count()))
                .from(meeting)
                .where(meeting.team.id.eq(teamId))
                .groupBy(meeting.meetingTime.startDate.year())
                .orderBy(meeting.meetingTime.startDate.year().desc())
                .fetch();
    }

    public List<YearMeetingsCountQueryDto> findWorkspaceYearMeetingsCount(Long workspaceUserId) {
        return queryFactory.select(new QYearMeetingsCountQueryDto(meeting.meetingTime.startDate.year(),
                meeting.meetingTime.startDate.count()))
                .from(attendee)
                .join(attendee.meeting, meeting)
                .where(attendee.workspaceUser.id.eq(workspaceUserId))
                .groupBy(meeting.meetingTime.startDate.year())
                .orderBy(meeting.meetingTime.startDate.year().desc())
                .fetch();
    }

    public List<MonthMeetingsCountQueryDto> findMyMonthMeetingsCount(List<Long> myWorkspaceUserIds, Year year) {
        return queryFactory.select(new QMonthMeetingsCountQueryDto(meeting.meetingTime.startDate.month(),
                meeting.meetingTime.startDate.count()))
                .from(attendee)
                .join(attendee.meeting, meeting)
                .where(yearEq(year),
                        attendee.workspaceUser.id.in(myWorkspaceUserIds))
                .groupBy(meeting.meetingTime.startDate.month())
                .orderBy(meeting.meetingTime.startDate.month().asc())
                .fetch();
    }

    private BooleanExpression yearEq(Year year) {
        return meeting.meetingTime.startDate.year().eq(year.getValue());
    }

    public List<MonthMeetingsCountQueryDto> findTeamMonthMeetingsCount(Long teamId, Year year) {
        return queryFactory.select(new QMonthMeetingsCountQueryDto(meeting.meetingTime.startDate.month(),
                meeting.meetingTime.startDate.count()))
                .from(meeting)
                .where(yearEq(year),
                        meeting.team.id.eq(teamId))
                .groupBy(meeting.meetingTime.startDate.month())
                .orderBy(meeting.meetingTime.startDate.month().asc())
                .fetch();
    }

    public List<MonthMeetingsCountQueryDto> findWorkspaceMonthMeetingsCount(Long workspaceUserId, Year year) {
        return queryFactory.select(new QMonthMeetingsCountQueryDto(meeting.meetingTime.startDate.month(),
                meeting.meetingTime.startDate.count()))
                .from(attendee)
                .join(attendee.meeting, meeting)
                .where(yearEq(year),
                        attendee.workspaceUser.id.eq(workspaceUserId))
                .groupBy(meeting.meetingTime.startDate.month())
                .orderBy(meeting.meetingTime.startDate.month().asc())
                .fetch();
    }

    public Optional<MeetingAndAdminsQueryDto> findWithTeamAndAdminsById(Long meetingId) {
        MeetingQueryDto meetingQueryDto = Optional.ofNullable(queryFactory.select(new QMeetingQueryDto(
                meeting.id, meeting.meetingInfo.name, meeting.meetingInfo.purpose,
                meeting.meetingTime.startDate, meeting.meetingTime.startTime, meeting.meetingTime.endTime,
                team.id, team.name
        )).from(meeting)
                .join(meeting.team, team)
                .where(meeting.id.eq(meetingId))
                .fetchOne())
                .orElseThrow(MeetingNotFoundException::new);

        List<AdminQueryDto> adminQueryDtos = queryFactory.select(new QAdminQueryDto(
                workspaceUser.id, workspaceUser.workspaceUserInfo.nickname
        )).from(attendee)
                .join(attendee.workspaceUser, workspaceUser)
                .where(attendee.meeting.id.eq(meetingId),
                        attendee.isMeetingAdmin.isTrue())
                .fetch();

        return Optional.of(MeetingAndAdminsQueryDto
                .fromQueryDto(meetingQueryDto, adminQueryDtos));
    }
}
