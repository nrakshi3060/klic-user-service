package com.klic.user_service.service;

import com.klic.user_service.external.resources.PostComment;
import com.klic.user_service.external.resources.PostCommentRepository;
import com.klic.user_service.mapper.PostCommentMapper;
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
class PostCommentServiceTest {

    @Mock
    private PostCommentRepository postCommentRepository;

    @Mock
    private PostCommentMapper postCommentMapper;

    @InjectMocks
    private PostCommentService postCommentService;

    @Test
    void getAllPostComments() {
        PostComment postComment = new PostComment();
        postComment.setId(UUID.randomUUID());
        com.klic.user_service.model.PostComment postCommentDto = new com.klic.user_service.model.PostComment();
        postCommentDto.setId(postComment.getId());

        when(postCommentRepository.findAll()).thenReturn(Collections.singletonList(postComment));
        when(postCommentMapper.toDto(postComment)).thenReturn(postCommentDto);

        List<com.klic.user_service.model.PostComment> result = postCommentService.getAllPostComments();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(postComment.getId(), result.get(0).getId());
        verify(postCommentRepository, times(1)).findAll();
    }

    @Test
    void getPostCommentById() {
        UUID id = UUID.randomUUID();
        PostComment postComment = new PostComment();
        postComment.setId(id);
        com.klic.user_service.model.PostComment postCommentDto = new com.klic.user_service.model.PostComment();
        postCommentDto.setId(id);

        when(postCommentRepository.findById(id)).thenReturn(Optional.of(postComment));
        when(postCommentMapper.toDto(postComment)).thenReturn(postCommentDto);

        Optional<com.klic.user_service.model.PostComment> result = postCommentService.getPostCommentById(id);

        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
        verify(postCommentRepository, times(1)).findById(id);
    }

    @Test
    void createPostComment() {
        UUID id = UUID.randomUUID();
        PostComment postComment = new PostComment();
        postComment.setId(id);
        com.klic.user_service.model.PostComment postCommentDto = new com.klic.user_service.model.PostComment();
        postCommentDto.setId(id);

        when(postCommentMapper.toEntity(postCommentDto)).thenReturn(postComment);
        when(postCommentRepository.save(postComment)).thenReturn(postComment);
        when(postCommentMapper.toDto(postComment)).thenReturn(postCommentDto);

        com.klic.user_service.model.PostComment result = postCommentService.createPostComment(postCommentDto);

        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(postCommentRepository, times(1)).save(postComment);
    }

    @Test
    void updatePostComment() {
        UUID id = UUID.randomUUID();
        PostComment existingPostComment = new PostComment();
        existingPostComment.setId(id);
        com.klic.user_service.model.PostComment postCommentDto = new com.klic.user_service.model.PostComment();
        postCommentDto.setId(id);

        when(postCommentRepository.findById(id)).thenReturn(Optional.of(existingPostComment));
        when(postCommentMapper.toEntity(postCommentDto)).thenReturn(existingPostComment);
        when(postCommentRepository.save(existingPostComment)).thenReturn(existingPostComment);
        when(postCommentMapper.toDto(existingPostComment)).thenReturn(postCommentDto);

        Optional<com.klic.user_service.model.PostComment> result = postCommentService.updatePostComment(id, postCommentDto);

        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
        verify(postCommentRepository, times(1)).save(existingPostComment);
    }

    @Test
    void deletePostComment() {
        UUID id = UUID.randomUUID();
        when(postCommentRepository.existsById(id)).thenReturn(true);

        boolean result = postCommentService.deletePostComment(id);

        assertTrue(result);
        verify(postCommentRepository, times(1)).deleteById(id);
    }

    @Test
    void deletePostComment_NotFound() {
        UUID id = UUID.randomUUID();
        when(postCommentRepository.existsById(id)).thenReturn(false);

        boolean result = postCommentService.deletePostComment(id);

        assertFalse(result);
        verify(postCommentRepository, never()).deleteById(id);
    }
}
