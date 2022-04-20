package com.cmc.meeron.attendee.adapter.out;

import com.cmc.meeron.attendee.application.port.out.response.MeetingAttendeesCountsByTeamQueryDto;
import com.cmc.meeron.attendee.application.port.out.response.QMeetingAttendeesCountsByTeamQueryDto;
import com.cmc.meeron.meeting.application.port.out.response.AttendStatusCountQueryDto;
import com.cmc.meeron.meeting.application.port.out.response.QAttendStatusCountQueryDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.cmc.meeron.attendee.domain.QAttendee.attendee;
import static com.cmc.meeron.meeting.domain.QMeeting.meeting;
import static com.cmc.meeron.team.domain.QTeam.team;
import static com.cmc.meeron.workspaceuser.domain.QWorkspaceUser.workspaceUser;

@Repository
@RequiredArgsConstructor
class AttendeeQuerydslRepository {

    private final JPAQueryFactory queryFactory;

    public List<AttendStatusCountQueryDto> countAttendStatusByMeetingIds(List<Long> meetingIds) {

        return queryFactory.select(new QAttendStatusCountQueryDto(
                attendee.meeting.id,
                attendee.attendStatus.stringValue(),
                attendee.attendStatus.count().intValue()
        )).from(attendee)
                .where(attendee.meeting.id.in(meetingIds))
                .groupBy(attendee.meeting.id,
                        attendee.attendStatus)
                .fetch();
    }

    public List<MeetingAttendeesCountsByTeamQueryDto> getMeetingAttendees(Long meetingId) {
        return queryFactory.select(new QMeetingAttendeesCountsByTeamQueryDto(
                team.id, team.name,
                attendee.attendStatus.stringValue(), attendee.attendStatus.stringValue().count().intValue()
        )).from(attendee)
                .join(attendee.meeting, meeting)
                .join(attendee.workspaceUser, workspaceUser)
                .join(workspaceUser.team, team)
                .where(meeting.id.eq(meetingId))
                .groupBy(team.id, attendee.attendStatus)
                .fetch();
    }
}
