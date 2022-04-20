package com.cmc.meeron.attendee.adapter.in.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Basic;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendeesStatusCountResponse {

    private int attend;
    private int absent;
    private int unknown;
}
