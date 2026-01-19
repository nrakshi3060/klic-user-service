package com.klic.user_service.service;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MinioStorageServiceTest {

    @Mock
    private MinioClient minioClient;

    private MinioStorageService minioStorageService;
    private final String bucketName = "test-bucket";

    @BeforeEach
    void setUp() {
        minioStorageService = new MinioStorageService(minioClient, bucketName);
    }

    @Test
    void uploadFile_Success() throws Exception {
        MultipartFile file = mock(MultipartFile.class);
        String originalFilename = "test.jpg";
        byte[] content = "test content".getBytes();
        InputStream inputStream = new ByteArrayInputStream(content);

        when(file.getOriginalFilename()).thenReturn(originalFilename);
        when(file.getInputStream()).thenReturn(inputStream);
        when(file.getSize()).thenReturn((long) content.length);
        when(file.getContentType()).thenReturn("image/jpeg");

        String result = minioStorageService.uploadFile(file);

        assertNotNull(result);
        assertTrue(result.endsWith("_" + originalFilename));
        verify(minioClient).putObject(any(PutObjectArgs.class));
    }

    @Test
    void uploadFile_Exception() throws Exception {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("test.jpg");
        when(file.getInputStream()).thenThrow(new RuntimeException("IO Error"));

        assertThrows(RuntimeException.class, () -> minioStorageService.uploadFile(file));
    }
}
