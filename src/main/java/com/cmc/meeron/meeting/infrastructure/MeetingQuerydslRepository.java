package com.cmc.meeron.meeting.infrastructure;

import com.cmc.meeron.meeting.domain.Meeting;
import com.cmc.meeron.meeting.domain.dto.MonthMeetingsCountDto;
import com.cmc.meeron.meeting.domain.dto.QMonthMeetingsCountDto;
import com.cmc.meeron.meeting.domain.dto.QYearMeetingsCountDto;
import com.cmc.meeron.meeting.domain.dto.YearMeetingsCountDto;
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
            return queryFactory.select(meeting.startDate.dayOfMonth())
                    .from(attendee)
                    .join(attendee.meeting, meeting)
                    .where(attendee.workspaceUser.id.in(searchIds),
                            yearMonthEq(yearMonth))
                    .fetch();
        }

        return queryFactory.select(meeting.startDate.dayOfMonth())
                .from(meeting)
                .where(searchTypePredicateOnlyWorkspaceAndTeam(searchType, searchIds)
                        .and(yearMonthEq(yearMonth)))
                .orderBy(meeting.startDate.dayOfMonth().asc())
                .fetch();
    }

    private BooleanExpression yearMonthEq(YearMonth yearMonth) {
        return meeting.startDate.year().eq(yearMonth.getYear())
                .and(meeting.startDate.month().eq(yearMonth.getMonthValue()));
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
        return meeting.startDate.eq(date);
    }

    public List<YearMeetingsCountDto> findYearMeetingsCount(String searchType, List<Long> searchIds) {
        if (searchType.equals(WORKSPACE_USER)) {
            return queryFactory.select(new QYearMeetingsCountDto(meeting.startDate.year(), meeting.startDate.count()))
                    .from(attendee)
                    .join(attendee.meeting, meeting)
                    .where(attendee.workspaceUser.id.in(searchIds))
                    .groupBy(meeting.startDate.year())
                    .orderBy(meeting.startDate.year().desc())
                    .fetch();
        }

        return queryFactory.select(new QYearMeetingsCountDto(meeting.startDate.year(), meeting.startDate.count()))
                .from(meeting)
                .where(searchTypePredicateOnlyWorkspaceAndTeam(searchType, searchIds))
                .groupBy(meeting.startDate.year())
                .orderBy(meeting.startDate.year().desc())
                .fetch();
    }

    public List<MonthMeetingsCountDto> findMonthMeetingsCount(String searchType, List<Long> searchIds, Year year) {
        if (searchType.equals(WORKSPACE_USER)) {
            return queryFactory.select(new QMonthMeetingsCountDto(meeting.startDate.month(), meeting.startDate.count()))
                    .from(attendee)
                    .join(attendee.meeting, meeting)
                    .where(attendee.workspaceUser.id.in(searchIds))
                    .groupBy(meeting.startDate.month())
                    .orderBy(meeting.startDate.month().asc())
                    .fetch();
        }

        return queryFactory.select(new QMonthMeetingsCountDto(meeting.startDate.month(), meeting.startDate.count()))
                .from(meeting)
                .where(searchTypePredicateOnlyWorkspaceAndTeam(searchType, searchIds))
                .groupBy(meeting.startDate.month())
                .orderBy(meeting.startDate.month().asc())
                .fetch();
    }
}