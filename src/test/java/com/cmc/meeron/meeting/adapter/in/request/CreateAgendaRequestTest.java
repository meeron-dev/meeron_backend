package com.cmc.meeron.meeting.adapter.in.request;

import com.cmc.meeron.meeting.application.port.in.request.CreateAgendaRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CreateAgendaRequestTest {

    @DisplayName("아젠다 요청 번호 순서대로 정렬 - 성공")
    @Test
    void sort_by_agenda_order_success() throws Exception {

        // given
        CreateAgendaRequest request = CreateAgendaRequest.builder()
                .agendas(new ArrayList<>(Arrays.asList(
                        CreateAgendaRequest.AgendaRequest.builder().order(3)
                                .issues(List.of(CreateAgendaRequest.IssueRequest.builder().issue("테스트이슈3").build())).build(),
                        CreateAgendaRequest.AgendaRequest.builder().order(4)
                                .issues(List.of(CreateAgendaRequest.IssueRequest.builder().issue("테스트이슈4").build())).build(),
                        CreateAgendaRequest.AgendaRequest.builder().order(1)
                                .issues(List.of(CreateAgendaRequest.IssueRequest.builder().issue("테스트이슈1").build())).build(),
                        CreateAgendaRequest.AgendaRequest.builder().order(2)
                                .issues(List.of(CreateAgendaRequest.IssueRequest.builder().issue("테스트이슈2").build())).build()
                ))).build();

        // when
        CreateAgendaRequestDto requestDto = request.toRequestDtoAndSortByAgendaOrder(1L);

        // then
        for (int i = 1; i <= request.getAgendas().size(); i++) {
            assertEquals(i, requestDto.getAgendaRequestDtos().get(i - 1).getAgendaOrder());
        }
    }
}
