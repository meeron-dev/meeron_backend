package com.cmc.meeron.meeting.adapter.out;

import com.cmc.meeron.meeting.domain.Meeting;
import com.cmc.meeron.meeting.application.port.out.response.MonthMeetingsCountQueryDto;
import com.cmc.meeron.meeting.application.port.out.response.YearMeetingsCountQueryDto;
import com.cmc.meeron.meeting.application.port.out.response.QMonthMeetingsCountQueryDto;
import com.cmc.meeron.meeting.application.port.out.response.QYearMeetingsCountQueryDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.util.List;

import static com.cmc.meeron.meeting.domain.QAttendee.attendee;
import static com.cmc.meeron.meeting.domain.QMeeting.meeting;
import static com.cmc.meeron.workspace.domain.QWorkspace.workspace;

@Repository
@RequiredArgsConstructor
class MeetingQuerydslRepository {

    private final String WORKSPACE = "WORKSPACE";
    private final String WORKSPACE_USER = "WORKSPACE_USER";
    private final String TEAM = "TEAM";

    private final JPAQueryFactory queryFactory;

    public List<Integer> findMeetingDays(String searchType, List<Long> searchIds, YearMonth yearMonth) {
        if (searchType.equals(WORKSPACE_USER)) {
            return queryFactory.select(meeting.meetingTime.startDate.dayOfMonth())
                    .from(attendee)
                    .join(attendee.meeting, meeting)
                    .where(attendee.workspaceUser.id.in(searchIds),
                            yearMonthEq(yearMonth))
                    .fetch();
        }

        return queryFactory.select(meeting.meetingTime.startDate.dayOfMonth())
                .from(meeting)
                .where(searchTypePredicateOnlyWorkspaceAndTeam(searchType, searchIds)
                        .and(yearMonthEq(yearMonth)))
                .orderBy(meeting.meetingTime.startDate.dayOfMonth().asc())
                .fetch();
    }

    private BooleanExpression yearMonthEq(YearMonth yearMonth) {
        return meeting.meetingTime.startDate.year().eq(yearMonth.getYear())
                .and(meeting.meetingTime.startDate.month().eq(yearMonth.getMonthValue()));
    }

    private BooleanExpression searchTypePredicateOnlyWorkspaceAndTeam(String searchType, List<Long> searchIds) {
        Long findId = searchIds.get(0);
        if (searchType.equals(WORKSPACE)) {
            return meeting.workspace.id.eq(findId);
        }
        return meeting.team.id.eq(findId);
    }

    public List<Meeting> findDayMeetings(String searchType, List<Long> searchIds, LocalDate date) {
        if (searchType.equals(WORKSPACE_USER)) {
            return queryFactory.selectFrom(meeting)
                    .join(meeting.workspace, workspace).fetchJoin()
                    .join(meeting.attendees.values, attendee)
                    .where(attendee.workspaceUser.id.in(searchIds),
                            dateEq(date))
                    .fetch();
        }

        return queryFactory.selectFrom(meeting)
                .where(searchTypePredicateOnlyWorkspaceAndTeam(searchType, searchIds),
                        dateEq(date))
                .fetch();
    }

    private BooleanExpression dateEq(LocalDate date) {
        return meeting.meetingTime.startDate.eq(date);
    }

    public List<YearMeetingsCountQueryDto> findYearMeetingsCount(String searchType, List<Long> searchIds) {
        if (searchType.equals(WORKSPACE_USER)) {
            return queryFactory.select(new QYearMeetingsCountQueryDto(meeting.meetingTime.startDate.year(),
                    meeting.meetingTime.startDate.count()))
                    .from(attendee)
                    .join(attendee.meeting, meeting)
                    .where(attendee.workspaceUser.id.in(searchIds))
                    .groupBy(meeting.meetingTime.startDate.year())
                    .orderBy(meeting.meetingTime.startDate.year().desc())
                    .fetch();
        }

        return queryFactory.select(new QYearMeetingsCountQueryDto(meeting.meetingTime.startDate.year(),
                meeting.meetingTime.startDate.count()))
                .from(meeting)
                .where(searchTypePredicateOnlyWorkspaceAndTeam(searchType, searchIds))
                .groupBy(meeting.meetingTime.startDate.year())
                .orderBy(meeting.meetingTime.startDate.year().desc())
                .fetch();
    }

    public List<MonthMeetingsCountQueryDto> findMonthMeetingsCount(String searchType, List<Long> searchIds, Year year) {
        if (searchType.equals(WORKSPACE_USER)) {
            return queryFactory.select(new QMonthMeetingsCountQueryDto(meeting.meetingTime.startDate.month(),
                    meeting.meetingTime.startDate.count()))
                    .from(attendee)
                    .join(attendee.meeting, meeting)
                    .where(attendee.workspaceUser.id.in(searchIds))
                    .groupBy(meeting.meetingTime.startDate.month())
                    .orderBy(meeting.meetingTime.startDate.month().asc())
                    .fetch();
        }

        return queryFactory.select(new QMonthMeetingsCountQueryDto(meeting.meetingTime.startDate.month(),
                meeting.meetingTime.startDate.count()))
                .from(meeting)
                .where(searchTypePredicateOnlyWorkspaceAndTeam(searchType, searchIds))
                .groupBy(meeting.meetingTime.startDate.month())
                .orderBy(meeting.meetingTime.startDate.month().asc())
                .fetch();
    }
}
