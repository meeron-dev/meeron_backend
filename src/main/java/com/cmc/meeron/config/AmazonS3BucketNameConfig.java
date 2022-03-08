package com.cmc.meeron.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class AmazonS3BucketNameConfig {

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;
}
