package com.cmc.meeron.file.application.port.in;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileManager {

    void saveAgendaFiles(Long agendaId, List<MultipartFile> files);
}
