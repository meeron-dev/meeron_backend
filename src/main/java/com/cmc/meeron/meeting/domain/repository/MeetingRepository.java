package com.cmc.meeron.meeting.domain.repository;

import com.cmc.meeron.meeting.domain.Meeting;

import java.util.List;

public interface MeetingRepository {

    List<Meeting> findTodayMeetings(Long workspaceId, Long workspaceUserId);
}
