package com.klic.user_service.service;

import com.klic.user_service.external.resources.Media;
import com.klic.user_service.external.resources.MediaRepository;
import com.klic.user_service.external.resources.Post;
import com.klic.user_service.external.resources.PostRepository;
import com.klic.user_service.external.resources.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MediaServiceTest {

    @Mock
    private MediaRepository mediaRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private MinioStorageService minioStorageService;

    @InjectMocks
    private MediaService mediaService;

    @Test
    void uploadMedia_Success() {
        UUID postId = UUID.randomUUID();
        MultipartFile file = mock(MultipartFile.class);
        String takenLocation = "POINT (30 10)";
        String uploadedLocation = "POINT (31 11)";

        Post post = new Post();
        post.setId(postId);
        User user = new User();
        user.setId(UUID.randomUUID());
        post.setUser(user);

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(minioStorageService.uploadFile(file)).thenReturn("some/path/to/media.jpg");
        when(mediaRepository.save(any(Media.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Media result = mediaService.uploadMedia(postId, file, takenLocation, uploadedLocation);

        assertNotNull(result);
        assertEquals("some/path/to/media.jpg", result.getMediaPath());
        assertEquals(post, result.getPost());
        assertEquals(user, result.getUser());
        assertNotNull(result.getTakenLocation());
        assertEquals(30.0, result.getTakenLocation().getX());
        assertEquals(10.0, result.getTakenLocation().getY());
        assertNotNull(result.getUploadedLocation());
        assertEquals(31.0, result.getUploadedLocation().getX());
        assertEquals(11.0, result.getUploadedLocation().getY());

        verify(postRepository).findById(postId);
        verify(minioStorageService).uploadFile(file);
        verify(mediaRepository).save(any(Media.class));
    }

    @Test
    void uploadMedia_PostNotFound() {
        UUID postId = UUID.randomUUID();
        MultipartFile file = mock(MultipartFile.class);

        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> 
            mediaService.uploadMedia(postId, file, null, null)
        );

        verify(postRepository).findById(postId);
        verifyNoInteractions(minioStorageService, mediaRepository);
    }

    @Test
    void uploadMedia_NullLocations() {
        UUID postId = UUID.randomUUID();
        MultipartFile file = mock(MultipartFile.class);

        Post post = new Post();
        post.setId(postId);
        User user = new User();
        user.setId(UUID.randomUUID());
        post.setUser(user);

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(minioStorageService.uploadFile(file)).thenReturn("some/path/to/media.jpg");
        when(mediaRepository.save(any(Media.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Media result = mediaService.uploadMedia(postId, file, null, null);

        assertNotNull(result);
        assertNull(result.getTakenLocation());
        assertNull(result.getUploadedLocation());
    }

    @Test
    void uploadMedia_EmptyLocations() {
        UUID postId = UUID.randomUUID();
        MultipartFile file = mock(MultipartFile.class);

        Post post = new Post();
        post.setId(postId);
        User user = new User();
        user.setId(UUID.randomUUID());
        post.setUser(user);

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(minioStorageService.uploadFile(file)).thenReturn("some/path/to/media.jpg");
        when(mediaRepository.save(any(Media.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Media result = mediaService.uploadMedia(postId, file, "", "");

        assertNotNull(result);
        assertNull(result.getTakenLocation());
        assertNull(result.getUploadedLocation());
    }

    @Test
    void uploadMedia_InvalidWkt() {
        UUID postId = UUID.randomUUID();
        MultipartFile file = mock(MultipartFile.class);
        String takenLocation = "INVALID WKT";

        Post post = new Post();
        post.setId(postId);
        post.setUser(new User());

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(minioStorageService.uploadFile(file)).thenReturn("path.jpg");

        assertThrows(RuntimeException.class, () -> 
            mediaService.uploadMedia(postId, file, takenLocation, null)
        );
    }
}
