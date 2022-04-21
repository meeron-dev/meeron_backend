package com.cmc.meeron.attendee.application.port.in.response;

import com.cmc.meeron.attendee.domain.AttendStatus;
import com.cmc.meeron.attendee.domain.Attendee;
import com.cmc.meeron.common.util.NicknameOrderByKoreanEnglishNumberSpecial;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeetingTeamAttendeesResponseDtoV2 {

    @Builder.Default
    private List<AttendeeResponseDto> attends = new ArrayList<>();

    @Builder.Default
    private List<AttendeeResponseDto> absents = new ArrayList<>();

    @Builder.Default
    private List<AttendeeResponseDto> unknowns = new ArrayList<>();

    public static MeetingTeamAttendeesResponseDtoV2 fromEntities(List<Attendee> attendees) {
        MeetingTeamAttendeesResponseDtoV2 responseDto = MeetingTeamAttendeesResponseDtoV2.builder().build();
        attendees.forEach(attendee -> {
            if (attendee.getAttendStatus().equals(AttendStatus.UNKNOWN)) {
                responseDto.addUnknowns(attendee);
            }
            else if (attendee.getAttendStatus().equals(AttendStatus.ATTEND)) {
                responseDto.addAttends(attendee);
            } else {
                responseDto.addAbsents(attendee);
            }
        });
        responseDto.sort();
        return responseDto;
    }

    private void sort() {
        attends.sort(NicknameOrderByKoreanEnglishNumberSpecial.getComparator());
        absents.sort(NicknameOrderByKoreanEnglishNumberSpecial.getComparator());
        unknowns.sort(NicknameOrderByKoreanEnglishNumberSpecial.getComparator());
    }

    private void addUnknowns(Attendee attendee) {
        this.unknowns.add(AttendeeResponseDto.from(attendee));
    }

    private void addAttends(Attendee attendee) {
        this.attends.add(AttendeeResponseDto.from(attendee));
    }

    private void addAbsents(Attendee attendee) {
        this.absents.add(AttendeeResponseDto.from(attendee));
    }
}
