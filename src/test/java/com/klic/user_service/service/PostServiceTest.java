package com.klic.user_service.service;

import com.klic.user_service.external.resources.Post;
import com.klic.user_service.external.resources.PostRepository;
import com.klic.user_service.mapper.PostMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private PostMapper postMapper;

    @InjectMocks
    private PostService postService;

    @Test
    void getAllPosts() {
        Post post = new Post();
        post.setId(UUID.randomUUID());
        com.klic.user_service.model.Post postDto = new com.klic.user_service.model.Post();
        postDto.setId(post.getId());

        when(postRepository.findAll()).thenReturn(Collections.singletonList(post));
        when(postMapper.toDto(post)).thenReturn(postDto);

        List<com.klic.user_service.model.Post> result = postService.getAllPosts();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(post.getId(), result.get(0).getId());
        verify(postRepository, times(1)).findAll();
    }

    @Test
    void getPostById() {
        UUID id = UUID.randomUUID();
        Post post = new Post();
        post.setId(id);
        com.klic.user_service.model.Post postDto = new com.klic.user_service.model.Post();
        postDto.setId(id);

        when(postRepository.findById(id)).thenReturn(Optional.of(post));
        when(postMapper.toDto(post)).thenReturn(postDto);

        Optional<com.klic.user_service.model.Post> result = postService.getPostById(id);

        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
        verify(postRepository, times(1)).findById(id);
    }

    @Test
    void createPost() {
        UUID id = UUID.randomUUID();
        Post post = new Post();
        post.setId(id);
        com.klic.user_service.model.Post postDto = new com.klic.user_service.model.Post();
        postDto.setId(id);

        when(postMapper.toEntity(postDto)).thenReturn(post);
        when(postRepository.save(post)).thenReturn(post);
        when(postMapper.toDto(post)).thenReturn(postDto);

        com.klic.user_service.model.Post result = postService.createPost(postDto);

        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(postRepository, times(1)).save(post);
    }

    @Test
    void updatePost() {
        UUID id = UUID.randomUUID();
        Post existingPost = new Post();
        existingPost.setId(id);
        com.klic.user_service.model.Post postDto = new com.klic.user_service.model.Post();
        postDto.setId(id);

        when(postRepository.findById(id)).thenReturn(Optional.of(existingPost));
        when(postMapper.toEntity(postDto)).thenReturn(existingPost);
        when(postRepository.save(existingPost)).thenReturn(existingPost);
        when(postMapper.toDto(existingPost)).thenReturn(postDto);

        Optional<com.klic.user_service.model.Post> result = postService.updatePost(id, postDto);

        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
        verify(postRepository, times(1)).save(existingPost);
    }

    @Test
    void deletePost() {
        UUID id = UUID.randomUUID();
        when(postRepository.existsById(id)).thenReturn(true);

        boolean result = postService.deletePost(id);

        assertTrue(result);
        verify(postRepository, times(1)).deleteById(id);
    }

    @Test
    void deletePost_NotFound() {
        UUID id = UUID.randomUUID();
        when(postRepository.existsById(id)).thenReturn(false);

        boolean result = postService.deletePost(id);

        assertFalse(result);
        verify(postRepository, never()).deleteById(id);
    }
}
