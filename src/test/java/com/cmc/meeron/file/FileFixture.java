package com.cmc.meeron.file;

import org.apache.http.entity.ContentType;
import org.springframework.mock.web.MockMultipartFile;

public class FileFixture {

    public static MockMultipartFile FILE = new MockMultipartFile("test.jpg",
            "test.jpg",
            ContentType.IMAGE_JPEG.getMimeType(),
            "테스트파일".getBytes());
}
