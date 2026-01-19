package com.klic.user_service.mapper;

import com.klic.user_service.external.resources.Post;
import com.klic.user_service.external.resources.PostType;
import com.klic.user_service.external.resources.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class PostMapperTest {

    private PostMapper postMapper;

    @BeforeEach
    public void setUp() {
        postMapper = Mappers.getMapper(PostMapper.class);
    }

    @Test
    public void testToDto() {
        Post post = new Post();
        post.setId(UUID.randomUUID());
        User user = new User();
        user.setId(UUID.randomUUID());
        post.setUser(user);
        post.setPostDescription("Test Description");
        post.setPostType(PostType.TEXT);

        com.klic.user_service.model.Post dto = postMapper.toDto(post);

        assertNotNull(dto);
        assertEquals(post.getId(), dto.getId());
        assertEquals(user.getId(), dto.getUserId());
        assertEquals(post.getPostDescription(), dto.getPostDescription());
        assertEquals(post.getPostType().name(), dto.getPostType().getValue());
    }

    @Test
    public void testToDto_Null() {
        assertNull(postMapper.toDto(null));
    }

    @Test
    public void testToEntity_Null() {
        assertNull(postMapper.toEntity(null));
    }
}
