package com.cmc.meeron.meeting;

import com.cmc.meeron.meeting.domain.Agenda;

import static com.cmc.meeron.meeting.MeetingFixture.MEETING;

public class AgendaFixture {

    public static final Agenda AGENDA1 = Agenda.builder()
            .id(3L)
            .agendaOrder(1)
            .meeting(MEETING)
            .name("테스트아젠다1")
            .build();

    public static final Agenda AGENDA2 = Agenda.builder()
            .id(4L)
            .agendaOrder(2)
            .meeting(MEETING)
            .name("테스트아젠다2")
            .build();
}
