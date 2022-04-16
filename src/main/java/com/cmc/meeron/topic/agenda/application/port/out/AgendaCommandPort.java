package com.cmc.meeron.topic.agenda.application.port.out;

import com.cmc.meeron.topic.agenda.domain.Agenda;

public interface AgendaCommandPort {

    Agenda save(Agenda agenda);
}
