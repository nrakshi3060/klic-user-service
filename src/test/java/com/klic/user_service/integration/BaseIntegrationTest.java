package com.klic.user_service.integration;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MinIOContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("integration")
public abstract class BaseIntegrationTest {

    @LocalServerPort
    protected int port;

    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(DockerImageName.parse("postgis/postgis:15-3.4-alpine")
            .asCompatibleSubstituteFor("postgres"))
            .withDatabaseName("klic")
            .withUsername("test")
            .withPassword("test");

    static final MinIOContainer minio = new MinIOContainer(DockerImageName.parse("minio/minio:latest"));

    @BeforeAll
    static void beforeAll() {
        postgres.start();
        minio.start();

        try {
            io.minio.MinioClient client = io.minio.MinioClient.builder()
                    .endpoint(minio.getS3URL())
                    .credentials(minio.getUserName(), minio.getPassword())
                    .build();

            boolean found = client.bucketExists(io.minio.BucketExistsArgs.builder().bucket("klic-media").build());
            if (!found) {
                client.makeBucket(io.minio.MakeBucketArgs.builder().bucket("klic-media").build());
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not create MinIO bucket", e);
        }
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
        minio.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.liquibase.enabled", () -> "true");
        
        registry.add("minio.url", minio::getS3URL);
        registry.add("minio.access-key", minio::getUserName);
        registry.add("minio.secret-key", minio::getPassword);
        registry.add("minio.bucket-name", () -> "klic-media");
    }
}
