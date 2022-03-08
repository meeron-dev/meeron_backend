package com.cmc.meeron.meeting;

import com.cmc.meeron.meeting.domain.Agenda;

public class AgendaFixture {

    public static final Agenda AGENDA1 = Agenda.builder()
            .id(3L)
            .agendaOrder(1)
            .build();

    public static final Agenda AGENDA2 = Agenda.builder()
            .id(4L)
            .agendaOrder(2)
            .build();
}
