package com.cmc.meeron.meeting.adapter.out;

import com.cmc.meeron.meeting.application.port.out.response.AttendStatusCountResponseDto;
import com.cmc.meeron.meeting.application.port.out.response.QAttendStatusCountResponseDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.cmc.meeron.meeting.domain.QAttendee.attendee;

@Repository
@RequiredArgsConstructor
class AttendeeQuerydslRepository {

    private final JPAQueryFactory queryFactory;

    public List<AttendStatusCountResponseDto> countAttendStatusByMeetingIds(List<Long> meetingIds) {

        return queryFactory.select(new QAttendStatusCountResponseDto(
                attendee.meeting.id,
                attendee.attendStatus.stringValue(),
                attendee.attendStatus.count().intValue()
        )).from(attendee)
                .where(attendee.meeting.id.in(meetingIds))
                .groupBy(attendee.meeting.id,
                        attendee.attendStatus)
                .fetch();
    }
}
