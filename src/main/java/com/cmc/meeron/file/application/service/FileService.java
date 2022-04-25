package com.cmc.meeron.file.application.service;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.util.IOUtils;
import com.cmc.meeron.common.exception.file.FileExtensionNotFoundException;
import com.cmc.meeron.common.exception.file.FileUploadException;
import com.cmc.meeron.common.exception.meeting.AgendaNotFoundException;
import com.cmc.meeron.file.application.port.in.FileManager;
import com.cmc.meeron.file.application.port.in.response.AgendaFileResponseDto;
import com.cmc.meeron.file.application.port.out.AgendaFileCommandPort;
import com.cmc.meeron.file.application.port.out.AgendaFileQueryPort;
import com.cmc.meeron.file.application.port.out.AgendaFileToAgendaQueryPort;
import com.cmc.meeron.file.application.port.out.StoragePort;
import com.cmc.meeron.file.domain.AgendaFile;
import com.cmc.meeron.topic.agenda.domain.Agenda;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
class FileService implements FileManager {

    private final StoragePort storagePort;
    private final AgendaFileCommandPort agendaFileCommandPort;
    private final AgendaFileToAgendaQueryPort fileAgendaQueryPort;
    private final AgendaFileQueryPort agendaFileQueryPort;

    @Override
    public void saveAgendaFiles(Long agendaId, List<MultipartFile> files) {
        Agenda agenda = fileAgendaQueryPort.findById(agendaId)
                .orElseThrow(AgendaNotFoundException::new);
        files.forEach(file -> {
            String originFileName = file.getOriginalFilename();
            String renameFileName = getRenameFileName(file);
            uploadToFileStorage(file, renameFileName);
            String url = storagePort.getUrl(renameFileName);
            agendaFileCommandPort.save(AgendaFile.of(agenda, originFileName, renameFileName, url));
        });
    }

    private String getRenameFileName(MultipartFile file) {
        return UUID.randomUUID()
                .toString()
                .concat(getFileExtension(
                        Objects.requireNonNull(file.getOriginalFilename())));
    }

    private String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new FileExtensionNotFoundException();
        }
    }

    private void uploadToFileStorage(MultipartFile file, String filename) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());

        try (InputStream inputStream = file.getInputStream()){
            byte[] bytes = IOUtils.toByteArray(inputStream);
            objectMetadata.setContentLength(bytes.length);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            storagePort.upload(filename, byteArrayInputStream, objectMetadata);
        } catch(IOException e) {
            throw new FileUploadException(String.format("파일 업로드 중 예외가 발생했습니다. (%s)", file.getOriginalFilename()));
        }
    }

    @Override
    public String saveProfileImage(MultipartFile file) {
        String originFileName = file.getOriginalFilename();
        String renameFileName = getRenameFileName(file);
        uploadToFileStorage(file, renameFileName);
        return storagePort.getUrl(renameFileName);
    }

    @Override
    public List<AgendaFileResponseDto> getAgendaFiles(Long agendaId) {
        List<AgendaFile> agendaFiles = agendaFileQueryPort.findByAgendaId(agendaId);
        return AgendaFileResponseDto.from(agendaFiles);
    }
}
