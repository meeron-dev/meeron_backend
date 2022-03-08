package com.cmc.meeron.meeting.application.port.out;

import com.cmc.meeron.meeting.domain.Agenda;
import com.cmc.meeron.meeting.domain.Issue;
import com.cmc.meeron.meeting.domain.Meeting;

import java.util.List;

public interface MeetingCommandPort {

    Long saveMeeting(Meeting meeting);

    Agenda saveAgenda(Agenda agenda);

    List<Issue> saveIssues(List<Issue> issues);
}
