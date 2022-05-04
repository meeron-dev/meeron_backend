package com.cmc.meeron.file.application.port.in;

import com.cmc.meeron.file.application.port.in.response.AgendaFileResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileManager {

    void saveAgendaFiles(Long agendaId, List<MultipartFile> files);

    String saveProfileImage(MultipartFile file);

    List<AgendaFileResponseDto> getAgendaFiles(Long agendaId);
}
