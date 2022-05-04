package com.cmc.meeron.topic.issue.application.port.in;

import com.cmc.meeron.topic.issue.application.port.in.response.IssueResponseDto;

import java.util.List;

public interface IssueQueryUseCase {

    List<IssueResponseDto> getAgendaIssues(Long agendaId);
}
