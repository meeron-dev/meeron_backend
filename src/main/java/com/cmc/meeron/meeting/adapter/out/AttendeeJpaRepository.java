package com.cmc.meeron.meeting.adapter.out;

import com.cmc.meeron.meeting.domain.Attendee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

interface AttendeeJpaRepository extends JpaRepository<Attendee, Long> {

    @Query("select a from Attendee a" +
            " join fetch a.workspaceUser wu" +
            " where a.meeting.id = :meetingId" +
            " and wu.team.id = :teamId")
    List<Attendee> findWithWorkspaceUserByMeetingIdTeamId(@Param("meetingId") Long meetingId,
                                                          @Param("teamId") Long teamId);
}
