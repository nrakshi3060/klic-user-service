package com.klic.user_service.service;

import com.klic.user_service.external.resources.CommentLike;
import com.klic.user_service.external.resources.CommentLikeRepository;
import com.klic.user_service.mapper.CommentLikeMapper;
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
class CommentLikeServiceTest {

    @Mock
    private CommentLikeRepository commentLikeRepository;

    @Mock
    private CommentLikeMapper commentLikeMapper;

    @InjectMocks
    private CommentLikeService commentLikeService;

    @Test
    void getAllCommentLikes() {
        CommentLike commentLike = new CommentLike();
        commentLike.setId(UUID.randomUUID());
        com.klic.user_service.model.CommentLike commentLikeDto = new com.klic.user_service.model.CommentLike();
        commentLikeDto.setId(commentLike.getId());

        when(commentLikeRepository.findAll()).thenReturn(Collections.singletonList(commentLike));
        when(commentLikeMapper.toDto(commentLike)).thenReturn(commentLikeDto);

        List<com.klic.user_service.model.CommentLike> result = commentLikeService.getAllCommentLikes();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(commentLike.getId(), result.get(0).getId());
        verify(commentLikeRepository, times(1)).findAll();
    }

    @Test
    void getCommentLikeById() {
        UUID id = UUID.randomUUID();
        CommentLike commentLike = new CommentLike();
        commentLike.setId(id);
        com.klic.user_service.model.CommentLike commentLikeDto = new com.klic.user_service.model.CommentLike();
        commentLikeDto.setId(id);

        when(commentLikeRepository.findById(id)).thenReturn(Optional.of(commentLike));
        when(commentLikeMapper.toDto(commentLike)).thenReturn(commentLikeDto);

        Optional<com.klic.user_service.model.CommentLike> result = commentLikeService.getCommentLikeById(id);

        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
        verify(commentLikeRepository, times(1)).findById(id);
    }

    @Test
    void createCommentLike() {
        UUID id = UUID.randomUUID();
        CommentLike commentLike = new CommentLike();
        commentLike.setId(id);
        com.klic.user_service.model.CommentLike commentLikeDto = new com.klic.user_service.model.CommentLike();
        commentLikeDto.setId(id);

        when(commentLikeMapper.toEntity(commentLikeDto)).thenReturn(commentLike);
        when(commentLikeRepository.save(commentLike)).thenReturn(commentLike);
        when(commentLikeMapper.toDto(commentLike)).thenReturn(commentLikeDto);

        com.klic.user_service.model.CommentLike result = commentLikeService.createCommentLike(commentLikeDto);

        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(commentLikeRepository, times(1)).save(commentLike);
    }

    @Test
    void updateCommentLike() {
        UUID id = UUID.randomUUID();
        CommentLike existingCommentLike = new CommentLike();
        existingCommentLike.setId(id);
        com.klic.user_service.model.CommentLike commentLikeDto = new com.klic.user_service.model.CommentLike();
        commentLikeDto.setId(id);

        when(commentLikeRepository.findById(id)).thenReturn(Optional.of(existingCommentLike));
        when(commentLikeMapper.toEntity(commentLikeDto)).thenReturn(existingCommentLike);
        when(commentLikeRepository.save(existingCommentLike)).thenReturn(existingCommentLike);
        when(commentLikeMapper.toDto(existingCommentLike)).thenReturn(commentLikeDto);

        Optional<com.klic.user_service.model.CommentLike> result = commentLikeService.updateCommentLike(id, commentLikeDto);

        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
        verify(commentLikeRepository, times(1)).save(existingCommentLike);
    }

    @Test
    void deleteCommentLike() {
        UUID id = UUID.randomUUID();
        when(commentLikeRepository.existsById(id)).thenReturn(true);

        boolean result = commentLikeService.deleteCommentLike(id);

        assertTrue(result);
        verify(commentLikeRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteCommentLike_NotFound() {
        UUID id = UUID.randomUUID();
        when(commentLikeRepository.existsById(id)).thenReturn(false);

        boolean result = commentLikeService.deleteCommentLike(id);

        assertFalse(result);
        verify(commentLikeRepository, never()).deleteById(id);
    }
}
