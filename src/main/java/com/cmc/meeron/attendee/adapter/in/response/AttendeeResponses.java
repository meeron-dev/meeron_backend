package com.cmc.meeron.attendee.adapter.in.response;

import com.cmc.meeron.attendee.application.port.in.response.AttendeeResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendeeResponses {

    @Builder.Default
    private List<AttendeeWorkspaceUserResponse> admins = new ArrayList<>();

    public static AttendeeResponses from(List<AttendeeResponseDto> responseDtos) {
        return AttendeeResponses.builder()
                .admins(responseDtos
                        .stream()
                        .map(AttendeeWorkspaceUserResponse::from)
                        .collect(Collectors.toList()))
                .build();
    }
}
