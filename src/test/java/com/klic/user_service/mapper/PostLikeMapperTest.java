package com.klic.user_service.mapper;

import com.klic.user_service.external.resources.Post;
import com.klic.user_service.external.resources.PostLike;
import com.klic.user_service.external.resources.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class PostLikeMapperTest {

    private PostLikeMapper postLikeMapper;

    @BeforeEach
    public void setUp() {
        postLikeMapper = Mappers.getMapper(PostLikeMapper.class);
    }

    @Test
    public void testToDto() {
        PostLike like = new PostLike();
        like.setId(UUID.randomUUID());
        
        Post post = new Post();
        post.setId(UUID.randomUUID());
        like.setPost(post);
        
        User user = new User();
        user.setId(UUID.randomUUID());
        like.setUser(user);

        com.klic.user_service.model.PostLike dto = postLikeMapper.toDto(like);

        assertNotNull(dto);
        assertEquals(like.getId(), dto.getId());
        assertEquals(post.getId(), dto.getPostId());
        assertEquals(user.getId(), dto.getUserId());
    }

    @Test
    public void testToDto_NullFields() {
        PostLike like = new PostLike();
        com.klic.user_service.model.PostLike dto = postLikeMapper.toDto(like);
        assertNotNull(dto);
        assertNull(dto.getPostId());
        assertNull(dto.getUserId());
    }

    @Test
    public void testToDto_Null() {
        assertNull(postLikeMapper.toDto(null));
    }

    @Test
    public void testToEntity() {
        com.klic.user_service.model.PostLike dto = new com.klic.user_service.model.PostLike();
        dto.setPostId(UUID.randomUUID());
        dto.setUserId(UUID.randomUUID());

        PostLike like = postLikeMapper.toEntity(dto);

        assertNotNull(like);
        assertEquals(dto.getPostId(), like.getPost().getId());
        assertEquals(dto.getUserId(), like.getUser().getId());
    }

    @Test
    public void testToEntity_Null() {
        assertNull(postLikeMapper.toEntity(null));
    }
}
