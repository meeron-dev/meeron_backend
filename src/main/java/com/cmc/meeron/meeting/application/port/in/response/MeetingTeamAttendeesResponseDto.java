package com.cmc.meeron.meeting.application.port.in.response;

import com.cmc.meeron.common.type.SortableByNickname;
import com.cmc.meeron.common.util.NicknameOrderByKoreanEnglishNumberSpecial;
import com.cmc.meeron.attendee.domain.AttendStatus;
import com.cmc.meeron.attendee.domain.Attendee;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeetingTeamAttendeesResponseDto {

    @Builder.Default
    private List<AttendeesResponseDto> attends = new ArrayList<>();

    @Builder.Default
    private List<AttendeesResponseDto> absents = new ArrayList<>();

    @Builder.Default
    private List<AttendeesResponseDto> unknowns = new ArrayList<>();

    public static MeetingTeamAttendeesResponseDto fromEntities(List<Attendee> attendees) {
        MeetingTeamAttendeesResponseDto responseDto = MeetingTeamAttendeesResponseDto.builder()
                .build();

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

    private void addAttends(Attendee attendee) {
        this.attends.add(AttendeesResponseDto.fromEntity(attendee));
    }

    private void addAbsents(Attendee attendee) {
        this.absents.add(AttendeesResponseDto.fromEntity(attendee));
    }

    private void addUnknowns(Attendee attendee) {
        this.unknowns.add(AttendeesResponseDto.fromEntity(attendee));
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AttendeesResponseDto implements SortableByNickname {

        private Long workspaceUserId;
        private String profileImageUrl;
        private String nickname;
        private String position;

        public static AttendeesResponseDto fromEntity(Attendee attendee) {
            return AttendeesResponseDto.builder()
                    .workspaceUserId(attendee.getWorkspaceUser().getId())
                    .profileImageUrl(attendee.getWorkspaceUser().getWorkspaceUserInfo().getProfileImageUrl())
                    .nickname(attendee.getWorkspaceUser().getWorkspaceUserInfo().getNickname())
                    .position(attendee.getWorkspaceUser().getWorkspaceUserInfo().getPosition())
                    .build();
        }
    }
}
