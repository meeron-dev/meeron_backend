package com.cmc.meeron.topic.agenda.adapter.out;

import com.cmc.meeron.meeting.application.port.out.response.FirstAgendaQueryDto;
import com.cmc.meeron.meeting.application.port.out.response.QFirstAgendaQueryDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.cmc.meeron.topic.agenda.domain.QAgenda.agenda;

@Repository
@RequiredArgsConstructor
class AgendaQuerydslRepository {

    private final JPAQueryFactory queryFactory;

    public List<FirstAgendaQueryDto> findFirstAgendaByMeetingIds(List<Long> meetingIds) {
        return queryFactory.select(new QFirstAgendaQueryDto(
                agenda.meeting.id, agenda.id, agenda.name
        )).from(agenda)
                .where(agenda.meeting.id.in(meetingIds),
                        agenda.agendaOrder.eq(1))
                .fetch();
    }
}
