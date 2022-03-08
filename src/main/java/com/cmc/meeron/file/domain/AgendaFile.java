package com.cmc.meeron.file.domain;

import com.cmc.meeron.common.domain.BaseEntity;
import com.cmc.meeron.meeting.domain.Agenda;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class AgendaFile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AGENDA_FILE_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AGENDA_ID", nullable = false)
    private Agenda agenda;

    @Column(nullable = false, length = 500)
    private String originFileName;

    @Column(nullable = false, length = 200)
    private String renameFileName;

    @Column(nullable = false, length = 200)
    private String url;

    public static AgendaFile of(Agenda agenda,
                                String originFileName,
                                String renameFileName,
                                String url) {
        return AgendaFile.builder()
                .agenda(agenda)
                .originFileName(originFileName)
                .renameFileName(renameFileName)
                .url(url)
                .build();
    }
}
