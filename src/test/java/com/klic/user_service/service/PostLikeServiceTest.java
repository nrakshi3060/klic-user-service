package com.klic.user_service.service;

import com.klic.user_service.external.resources.PostLike;
import com.klic.user_service.external.resources.PostLikeRepository;
import com.klic.user_service.mapper.PostLikeMapper;
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
class PostLikeServiceTest {

    @Mock
    private PostLikeRepository postLikeRepository;

    @Mock
    private PostLikeMapper postLikeMapper;

    @InjectMocks
    private PostLikeService postLikeService;

    @Test
    void getAllPostLikes() {
        PostLike postLike = new PostLike();
        postLike.setId(UUID.randomUUID());
        com.klic.user_service.model.PostLike postLikeDto = new com.klic.user_service.model.PostLike();
        postLikeDto.setId(postLike.getId());

        when(postLikeRepository.findAll()).thenReturn(Collections.singletonList(postLike));
        when(postLikeMapper.toDto(postLike)).thenReturn(postLikeDto);

        List<com.klic.user_service.model.PostLike> result = postLikeService.getAllPostLikes();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(postLike.getId(), result.get(0).getId());
        verify(postLikeRepository, times(1)).findAll();
    }

    @Test
    void getPostLikeById() {
        UUID id = UUID.randomUUID();
        PostLike postLike = new PostLike();
        postLike.setId(id);
        com.klic.user_service.model.PostLike postLikeDto = new com.klic.user_service.model.PostLike();
        postLikeDto.setId(id);

        when(postLikeRepository.findById(id)).thenReturn(Optional.of(postLike));
        when(postLikeMapper.toDto(postLike)).thenReturn(postLikeDto);

        Optional<com.klic.user_service.model.PostLike> result = postLikeService.getPostLikeById(id);

        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
        verify(postLikeRepository, times(1)).findById(id);
    }

    @Test
    void createPostLike() {
        UUID id = UUID.randomUUID();
        PostLike postLike = new PostLike();
        postLike.setId(id);
        com.klic.user_service.model.PostLike postLikeDto = new com.klic.user_service.model.PostLike();
        postLikeDto.setId(id);

        when(postLikeMapper.toEntity(postLikeDto)).thenReturn(postLike);
        when(postLikeRepository.save(postLike)).thenReturn(postLike);
        when(postLikeMapper.toDto(postLike)).thenReturn(postLikeDto);

        com.klic.user_service.model.PostLike result = postLikeService.createPostLike(postLikeDto);

        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(postLikeRepository, times(1)).save(postLike);
    }

    @Test
    void updatePostLike() {
        UUID id = UUID.randomUUID();
        PostLike existingPostLike = new PostLike();
        existingPostLike.setId(id);
        com.klic.user_service.model.PostLike postLikeDto = new com.klic.user_service.model.PostLike();
        postLikeDto.setId(id);

        when(postLikeRepository.findById(id)).thenReturn(Optional.of(existingPostLike));
        when(postLikeMapper.toEntity(postLikeDto)).thenReturn(existingPostLike);
        when(postLikeRepository.save(existingPostLike)).thenReturn(existingPostLike);
        when(postLikeMapper.toDto(existingPostLike)).thenReturn(postLikeDto);

        Optional<com.klic.user_service.model.PostLike> result = postLikeService.updatePostLike(id, postLikeDto);

        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
        verify(postLikeRepository, times(1)).save(existingPostLike);
    }

    @Test
    void deletePostLike() {
        UUID id = UUID.randomUUID();
        when(postLikeRepository.existsById(id)).thenReturn(true);

        boolean result = postLikeService.deletePostLike(id);

        assertTrue(result);
        verify(postLikeRepository, times(1)).deleteById(id);
    }

    @Test
    void deletePostLike_NotFound() {
        UUID id = UUID.randomUUID();
        when(postLikeRepository.existsById(id)).thenReturn(false);

        boolean result = postLikeService.deletePostLike(id);

        assertFalse(result);
        verify(postLikeRepository, never()).deleteById(id);
    }
}
