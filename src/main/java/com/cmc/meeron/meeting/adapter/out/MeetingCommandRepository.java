package com.cmc.meeron.meeting.adapter.out;

import com.cmc.meeron.meeting.application.port.out.MeetingCommandPort;
import com.cmc.meeron.meeting.domain.Agenda;
import com.cmc.meeron.meeting.domain.Issue;
import com.cmc.meeron.meeting.domain.Meeting;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
class MeetingCommandRepository implements MeetingCommandPort {

    private final MeetingJpaRepository meetingJpaRepository;
    private final AgendaJpaRepository agendaJpaRepository;
    private final IssueJpaRepository issueJpaRepository;

    @Override
    public Long saveMeeting(Meeting meeting) {
        return meetingJpaRepository.save(meeting)
                .getId();
    }

    @Override
    public Agenda saveAgenda(Agenda agenda) {
        return agendaJpaRepository.save(agenda);
    }

    @Override
    public List<Issue> saveIssues(List<Issue> issues) {
        return issueJpaRepository.saveAll(issues);
    }
}
