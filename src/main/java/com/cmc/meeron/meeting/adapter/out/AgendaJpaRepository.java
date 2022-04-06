package com.cmc.meeron.meeting.adapter.out;

import com.cmc.meeron.meeting.domain.Agenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

interface AgendaJpaRepository extends JpaRepository<Agenda, Long> {

    @Query(
            "select count(a)" +
            " from Agenda a" +
            " join a.meeting m on m.id = a.meeting.id" +
            " where m.id = :meetingId"
    )
    long countsByMeetingId(@Param("meetingId") Long meetingId);

    Optional<Agenda> findByMeetingIdAndAgendaOrder(Long meetingId, int agendaOrder);

    @Query(
            "select a" +
            " from Agenda a" +
            " where a.meeting.id in :meetingIds" +
            " order by a.id"
    )
    List<Agenda> findByMeetingIds(@Param("meetingIds") List<Long> meetingIds);
}
