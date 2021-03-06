package com.cmc.meeron.file.adapter.out;

import com.cmc.meeron.file.domain.AgendaFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

interface AgendaFileJpaRepository extends JpaRepository<AgendaFile, Long> {

    @Query(
            "select count(af)" +
            " from AgendaFile af" +
            " join af.agenda a on af.agenda.id = a.id" +
            " join a.meeting m on a.meeting.id = m.id" +
            " where m.id = :meetingId"
    )
    long countByMeetingId(@Param("meetingId") Long meetingId);

    List<AgendaFile> findByAgendaId(Long agendaId);
}
