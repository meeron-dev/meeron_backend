package com.cmc.meeron.meeting.adapter.in.request;

import com.cmc.meeron.meeting.application.port.in.request.CreateAgendaRequestDto;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateAgendaRequest {

    @Size(min = 1, max = 5, message = "아젠다는 최소 1개 최대 5개 까지 생성 가능합니다.")
    @Valid
    private List<AgendaRequest> agendas;

    public CreateAgendaRequestDto toRequestDto(Long meetingId) {
        return CreateAgendaRequestDto.builder()
                .meetingId(meetingId)
                .agendaRequestDtos(agendas
                        .stream()
                        .map(request -> CreateAgendaRequestDto.AgendaRequestDto.builder()
                                .agendaOrder(request.getOrder())
                                .agendaName(request.getName())
                                .issues(request.getIssues()
                                        .stream()
                                        .map(IssueRequest::getIssue)
                                        .collect(Collectors.toList()))
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AgendaRequest {

        private int order;
        @NotBlank(message = "아젠다를 48자 내로 입력해주세요.")
        @Length(max = 48, message = "아젠다를 48자 내로 입력해주세요.")
        private String name;
        private List<IssueRequest> issues;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class IssueRequest {
        private String issue;
    }
}
