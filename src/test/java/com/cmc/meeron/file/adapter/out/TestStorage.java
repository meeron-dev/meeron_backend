package com.cmc.meeron.file.adapter.out;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.cmc.meeron.file.application.port.out.StoragePort;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;

@Component
@Profile("test")
@RequiredArgsConstructor
public class TestStorage implements StoragePort {

    @Override
    public void upload(String filename, ByteArrayInputStream byteArrayInputStream, ObjectMetadata objectMetadata) {
        // empty
    }

    @Override
    public String getUrl(String renameFileName) {
        return renameFileName;
    }
}
