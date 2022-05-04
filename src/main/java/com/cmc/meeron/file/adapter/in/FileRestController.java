package com.cmc.meeron.file.adapter.in;

import com.cmc.meeron.file.adapter.in.response.AgendaFileResponses;
import com.cmc.meeron.file.application.port.in.FileManager;
import com.cmc.meeron.file.application.port.in.response.AgendaFileResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class FileRestController {

    private final FileManager fileManager;

    @PostMapping("/agendas/{agendaId}/files")
    @ResponseStatus(HttpStatus.CREATED)
    public void createAgendaFiles(@PathVariable Long agendaId,
                                  @RequestPart("files") List<MultipartFile> files) {
        fileManager.saveAgendaFiles(agendaId, files);
    }

    @GetMapping("/agendas/{agendaId}/files")
    @ResponseStatus(HttpStatus.OK)
    public AgendaFileResponses createAgendaFiles(@PathVariable Long agendaId) {
        List<AgendaFileResponseDto> responseDtos = fileManager.getAgendaFiles(agendaId);
        return AgendaFileResponses.from(responseDtos);
    }
}
