package com.cmc.meeron.topic.issue.domain;

import com.cmc.meeron.common.domain.BaseEntity;
import com.cmc.meeron.topic.agenda.domain.Agenda;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Issue extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ISSUE_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AGENDA_ID")
    private Agenda agenda;

    @Column(length = 1000)
    private String contents;

    @Column(length = 1000)
    private String issueResult;
}
