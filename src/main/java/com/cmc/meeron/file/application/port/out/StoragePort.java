package com.cmc.meeron.file.application.port.out;

import com.amazonaws.services.s3.model.ObjectMetadata;

import java.io.ByteArrayInputStream;

public interface StoragePort {

    void upload(String filename, ByteArrayInputStream byteArrayInputStream, ObjectMetadata objectMetadata);

    String getUrl(String renameFileName);
}
