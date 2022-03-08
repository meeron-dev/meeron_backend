package com.cmc.meeron.file.adapter.out;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.cmc.meeron.config.AmazonS3BucketNameConfig;
import com.cmc.meeron.file.application.port.out.StoragePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;

@Component
@RequiredArgsConstructor
class S3Storage implements StoragePort {

    private final AmazonS3Client amazonS3Client;
    private final AmazonS3BucketNameConfig bucketName;
    private final String PATH = "/files";

    @Override
    public void upload(String filename, ByteArrayInputStream inputStream, ObjectMetadata objectMetadata) {
        amazonS3Client.putObject(
                new PutObjectRequest(
                        bucketName.getBucketName() + PATH,
                        filename,
                        inputStream,
                        objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead)
        );
    }

    @Override
    public String getUrl(String filename) {
        return String.valueOf(amazonS3Client.getUrl(bucketName.getBucketName() + PATH, filename));
    }
}
