package com.cmc.meeron.meeting.adapter.out;

import com.cmc.meeron.attendee.domain.Attendee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

interface AttendeeJpaRepository extends JpaRepository<Attendee, Long> {

    @Query(
            "select a from Attendee a" +
            " join fetch a.workspaceUser wu" +
            " where a.meeting.id = :meetingId" +
            " and wu.team.id = :teamId")
    List<Attendee> findWithWorkspaceUserByMeetingIdTeamId(@Param("meetingId") Long meetingId,
                                                          @Param("teamId") Long teamId);

    Optional<Attendee> findByMeetingIdAndWorkspaceUserId(Long meetingId, Long workspaceUserId);

    @Query(
            "select a" +
            " from Attendee a" +
            " join fetch a.workspaceUser wu" +
            " where a.meeting.id in :meetingIds" +
            " and a.isMeetingAdmin = true"
    )
    List<Attendee> findMeetingAdminsByMeetingIds(@Param("meetingIds") List<Long> meetingIds);
}
