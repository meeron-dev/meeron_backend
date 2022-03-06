package com.cmc.meeron.meeting.adapter.out;

import com.cmc.meeron.meeting.domain.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

interface MeetingJpaRepository
        extends JpaRepository<Meeting, Long>{

    @Query(
            "select m" +
            " from Meeting m join fetch m.attendees.values a" +
            " where m.workspace.id = :workspaceId" +
            " and a.workspaceUser.id = :workspaceUserId" +
            " and m.meetingTime.startDate = :todayDate" +
            " order by m.meetingTime.startDate asc"
    )
    List<Meeting> findTodayMeetings(@Param("workspaceId") Long workspaceId,
                                    @Param("workspaceUserId") Long workspaceUserId,
                                    @Param("todayDate") LocalDate todayDate);
}
