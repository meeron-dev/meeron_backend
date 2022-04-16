package com.cmc.meeron.topic.agenda.application.port.in.request;

import com.cmc.meeron.topic.agenda.domain.Agenda;
import com.cmc.meeron.topic.issue.domain.Issue;
import com.cmc.meeron.meeting.domain.Meeting;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateAgendaRequestDto {

    private Long meetingId;
    @Builder.Default
    private List<AgendaRequestDto> agendaRequestDtos = new ArrayList<>();

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AgendaRequestDto {
        private int agendaOrder;
        private String agendaName;
        @Builder.Default
        private List<String> issues = new ArrayList<>();

        public Agenda createAgenda(Meeting meeting) {
            return Agenda.builder()
                    .agendaOrder(agendaOrder)
                    .name(agendaName)
                    .meeting(meeting)
                    .build();
        }

        public List<Issue> createIssues(Agenda agenda) {
            return issues.stream()
                    .map(issue -> Issue.builder()
                            .agenda(agenda)
                            .contents(issue)
                            .build())
                    .collect(Collectors.toList());
        }
    }
}
