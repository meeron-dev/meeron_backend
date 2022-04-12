package com.cmc.meeron.meeting;

import com.cmc.meeron.file.domain.AgendaFile;

import static com.cmc.meeron.meeting.AgendaFixture.AGENDA1;

public class AgendaFileFixture {

    public static final AgendaFile AGENDA_FILE_1 = AgendaFile.builder()
            .id(1L)
            .agenda(AGENDA1)
            .url("aws.s3.com/123123")
            .renameFileName("수정된파일명")
            .originFileName("원본파일명")
            .build();
}
